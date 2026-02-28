package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    // Overlap logic:
    // Overlap exists if: newCheckIn < existingCheckOut AND newCheckOut > existingCheckIn
    @Override
    public boolean hasOverlap(String roomType, Date checkIn, Date checkOut) throws Exception {
        String sql =
                "SELECT 1 FROM reservations " +
                        "WHERE status='ACTIVE' AND room_type=? " +
                        "AND (? < check_out AND ? > check_in) " +
                        "LIMIT 1";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, roomType);
            ps.setDate(2, checkIn);
            ps.setDate(3, checkOut);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int create(Reservation r) throws Exception {
        String sql =
                "INSERT INTO reservations(guest_name, phone, guest_email, address, room_type, check_in, check_out, guests, notes," +
                        " nights, total_amount, deposit, balance, payment_method, payment_status, created_by) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1..9
            ps.setString(1, r.guestName);
            ps.setString(2, r.phone);
            ps.setString(3, r.email);         // guest_email
            ps.setString(4, r.address);
            ps.setString(5, r.roomType);
            ps.setDate(6, Date.valueOf(r.checkIn));
            ps.setDate(7, Date.valueOf(r.checkOut));
            ps.setInt(8, r.guests);
            ps.setString(9, r.notes);

            // 10..13
            ps.setInt(10, r.nights);
            ps.setDouble(11, r.totalAmount);
            ps.setDouble(12, r.deposit);
            ps.setDouble(13, r.balance);

            // 14..16
            ps.setString(14, r.paymentMethod);
            ps.setString(15, r.paymentStatus);
            ps.setString(16, r.createdBy);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                throw new RuntimeException("Failed to generate reservation id");
            }
        }
    }

    @Override
    public Reservation findById(int id) throws Exception {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    @Override
    public List<Reservation> search(String phone, Integer reservationId) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM reservations WHERE status='ACTIVE' ");
        List<Object> params = new ArrayList<>();

        if (reservationId != null) {
            sql.append(" AND reservation_id = ? ");
            params.add(reservationId);
        }
        if (phone != null && !phone.trim().isEmpty()) {
            sql.append(" AND phone = ? ");
            params.add(phone.trim());
        }

        sql.append(" ORDER BY created_at DESC LIMIT 200");

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object v = params.get(i);
                if (v instanceof Integer) ps.setInt(i + 1, (int) v);
                else ps.setString(i + 1, (String) v);
            }

            List<Reservation> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
            return out;
        }
    }

    @Override
    public boolean cancel(int reservationId, String cancelledBy) throws Exception {
        String sql =
                "UPDATE reservations " +
                        "SET status='CANCELLED', payment_status='UNPAID' " +
                        "WHERE reservation_id=? AND status='ACTIVE'";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            int updated = ps.executeUpdate();
            return updated == 1;
        }
    }

    @Override
    public boolean addPayment(int reservationId, double addAmount) throws Exception {
        // We update deposit and recompute balance + status in SQL safely
        String sql =
                "UPDATE reservations " +
                        "SET deposit = LEAST(total_amount, deposit + ?), " +
                        "    balance = GREATEST(0, total_amount - LEAST(total_amount, deposit + ?)), " +
                        "    payment_status = CASE " +
                        "        WHEN LEAST(total_amount, deposit + ?) >= total_amount THEN 'PAID' " +
                        "        WHEN LEAST(total_amount, deposit + ?) > 0 THEN 'PARTIAL' " +
                        "        ELSE 'UNPAID' END " +
                        "WHERE reservation_id=? AND status='ACTIVE'";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, addAmount);
            ps.setDouble(2, addAmount);
            ps.setDouble(3, addAmount);
            ps.setDouble(4, addAmount);
            ps.setInt(5, reservationId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePayment(int reservationId, double newDeposit, double newBalance, String newStatus) throws Exception {
        String sql = "UPDATE reservations SET deposit=?, balance=?, payment_status=? WHERE reservation_id=? AND status='ACTIVE'";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, newDeposit);
            ps.setDouble(2, newBalance);
            ps.setString(3, newStatus);
            ps.setInt(4, reservationId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<String[]> getAvailability() throws Exception {
        String sql = "SELECT room_type, check_in, check_out, guest_name, reservation_id " +
                "FROM reservations WHERE status='ACTIVE' ORDER BY room_type, check_in";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<String[]> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new String[]{
                        rs.getString("room_type"),
                        rs.getString("check_in"),
                        rs.getString("check_out"),
                        rs.getString("guest_name"),
                        String.valueOf(rs.getInt("reservation_id"))
                });
            }
            return out;
        }
    }

    @Override
    public int countOverlapsForType(String roomType, java.sql.Date from, java.sql.Date to) throws Exception {
        String sql =
                "SELECT COUNT(*) AS c FROM reservations " +
                        "WHERE status='ACTIVE' AND room_type=? " +
                        "AND (? < check_out AND ? > check_in)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, roomType);
            ps.setDate(2, from);
            ps.setDate(3, to);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("c");
            }
        }
    }

    @Override
    public List<String[]> overlappingBookings(String roomType, java.sql.Date from, java.sql.Date to) throws Exception {
        String sql =
                "SELECT reservation_id, guest_name, check_in, check_out " +
                        "FROM reservations " +
                        "WHERE status='ACTIVE' AND room_type=? " +
                        "AND (? < check_out AND ? > check_in) " +
                        "ORDER BY check_in";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, roomType);
            ps.setDate(2, from);
            ps.setDate(3, to);
            try (ResultSet rs = ps.executeQuery()) {
                List<String[]> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new String[]{
                            String.valueOf(rs.getInt("reservation_id")),
                            rs.getString("guest_name"),
                            rs.getString("check_in"),
                            rs.getString("check_out")
                    });
                }
                return out;
            }
        }
    }

    private Reservation map(ResultSet rs) throws Exception {
        Reservation r = new Reservation();
        r.reservationId = rs.getInt("reservation_id");
        r.guestName = rs.getString("guest_name");
        r.phone = rs.getString("phone");
        r.email = rs.getString("guest_email");
        r.address = rs.getString("address");
        r.roomType = rs.getString("room_type");
        r.checkIn = rs.getDate("check_in").toLocalDate();
        r.checkOut = rs.getDate("check_out").toLocalDate();
        r.guests = rs.getInt("guests");
        r.notes = rs.getString("notes");
        r.nights = rs.getInt("nights");
        r.totalAmount = rs.getDouble("total_amount");
        r.deposit = rs.getDouble("deposit");
        r.balance = rs.getDouble("balance");
        r.paymentMethod = rs.getString("payment_method");
        r.paymentStatus = rs.getString("payment_status");
        r.createdBy = rs.getString("created_by");
        r.status = rs.getString("status");
        return r;
    }
}