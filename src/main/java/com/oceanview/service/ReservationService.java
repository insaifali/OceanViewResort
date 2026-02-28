package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.ReservationDAOImpl;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.RoomDAOImpl;
import com.oceanview.model.Reservation;
import com.oceanview.util.DateUtil;

import java.time.LocalDate;

public class ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final RoomDAO roomDAO = new RoomDAOImpl();

    public int createReservation(Reservation r) throws Exception {
        // Basic validation
        if (r.guestName == null || r.guestName.trim().isEmpty()) throw new IllegalArgumentException("Guest name required");
        if (r.phone == null || r.phone.trim().isEmpty()) throw new IllegalArgumentException("Phone required");
        if (r.roomType == null || r.roomType.trim().isEmpty()) throw new IllegalArgumentException("Room type required");
        if (r.checkIn == null || r.checkOut == null) throw new IllegalArgumentException("Dates required");

        if (!r.checkOut.isAfter(r.checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        int nights = DateUtil.nights(r.checkIn, r.checkOut);
        if (nights <= 0) throw new IllegalArgumentException("Invalid nights");

        // capacity-based overlap check
        int overlaps = reservationDAO.countOverlapsForType(
                r.roomType,
                java.sql.Date.valueOf(r.checkIn),
                java.sql.Date.valueOf(r.checkOut)
        );
        int capacity = new com.oceanview.dao.RoomDAOImpl().getAllRooms().stream()
                .filter(x -> x.roomType.equals(r.roomType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Room type not found: " + r.roomType))
                .capacity;

        if (overlaps >= capacity) {
            throw new IllegalArgumentException("Selected room type is not available for those dates");
        }

        // Billing
        double rate = roomDAO.getRate(r.roomType);
        double total = rate * nights;

        double deposit = Math.max(0, r.deposit);
        if (deposit > total) deposit = total;

        double balance = total - deposit;

        String payStatus;
        if (deposit == 0) payStatus = "UNPAID";
        else if (deposit < total) payStatus = "PARTIAL";
        else payStatus = "PAID";

        r.nights = nights;
        r.totalAmount = total;
        r.deposit = deposit;
        r.balance = balance;
        r.paymentStatus = payStatus;

        return reservationDAO.create(r);
    }

    public Reservation getReservation(int id) throws Exception {
        return reservationDAO.findById(id);
    }

    public java.util.List<Reservation> search(String phone, Integer reservationId) throws Exception {
        return reservationDAO.search(phone, reservationId);
    }

    public void cancelReservation(int reservationId, String role) throws Exception {
        if (!"ADMIN".equals(role) && !"RECEPTIONIST".equals(role)) {
            throw new IllegalArgumentException("You are not allowed to cancel reservations");
        }

        boolean ok = reservationDAO.cancel(reservationId, "SYSTEM");
        if (!ok) {
            throw new IllegalArgumentException("Reservation not found or already cancelled");
        }
    }

    public boolean addPayment(int id, double addAmount) throws Exception {
        if (addAmount <= 0) throw new IllegalArgumentException("Payment must be greater than 0");

        Reservation r = reservationDAO.findById(id);
        if (r == null) throw new IllegalArgumentException("Reservation not found");
        if (!"ACTIVE".equals(r.status)) throw new IllegalArgumentException("Reservation is not active");

        double total = r.totalAmount;
        double newDeposit = Math.min(total, r.deposit + addAmount);
        double newBalance = Math.max(0, total - newDeposit);

        String newStatus;
        if (newDeposit <= 0) newStatus = "UNPAID";
        else if (newDeposit < total) newStatus = "PARTIAL";
        else newStatus = "PAID";

        return reservationDAO.updatePayment(id, newDeposit, newBalance, newStatus);
    }

    public java.util.List<String[]> availability() throws Exception {
        return reservationDAO.getAvailability();
    }

    public java.util.List<com.oceanview.model.AvailabilityResult> checkAvailability(java.time.LocalDate from, java.time.LocalDate to) throws Exception {
        if (from == null || to == null) throw new IllegalArgumentException("Dates required");
        if (!to.isAfter(from)) throw new IllegalArgumentException("To date must be after From date");

        java.sql.Date f = java.sql.Date.valueOf(from);
        java.sql.Date t = java.sql.Date.valueOf(to);

        java.util.List<com.oceanview.model.RoomInfo> rooms = new com.oceanview.dao.RoomDAOImpl().getAllRooms();
        java.util.List<com.oceanview.model.AvailabilityResult> out = new java.util.ArrayList<>();

        for (com.oceanview.model.RoomInfo room : rooms) {
            int overlaps = reservationDAO.countOverlapsForType(room.roomType, f, t);
            int available = Math.max(0, room.capacity - overlaps);

            com.oceanview.model.AvailabilityResult ar = new com.oceanview.model.AvailabilityResult();
            ar.roomType = room.roomType;
            ar.capacity = room.capacity;
            ar.bookedCount = overlaps;
            ar.availableCount = available;
            ar.available = available > 0;
            ar.bookings = reservationDAO.overlappingBookings(room.roomType, f, t);

            out.add(ar);
        }
        return out;
    }
}

