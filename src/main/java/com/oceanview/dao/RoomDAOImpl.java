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
}