package com.oceanview.dao;

import com.oceanview.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public double getRate(String roomType) throws Exception {
        String sql = "SELECT price_per_night FROM rooms WHERE room_type = ? AND is_active = 1";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, roomType);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new RuntimeException("Room type not found: " + roomType);
                return rs.getDouble("price_per_night");
            }
        }
    }

    @Override
    public java.util.List<com.oceanview.model.RoomInfo> getAllRooms() throws Exception {
        String sql = "SELECT room_type, price_per_night, room_capacity FROM rooms WHERE is_active=1 ORDER BY room_type";
        try (java.sql.Connection c = com.oceanview.util.DB.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            java.util.List<com.oceanview.model.RoomInfo> out = new java.util.ArrayList<>();
            while (rs.next()) {
                com.oceanview.model.RoomInfo r = new com.oceanview.model.RoomInfo();
                r.roomType = rs.getString("room_type");
                r.rate = rs.getDouble("price_per_night");
                r.capacity = rs.getInt("room_capacity");
                out.add(r);
            }
            return out;
        }
    }
}