package com.oceanview.dao;

import com.oceanview.model.User;
import com.oceanview.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean usernameExists(String username) throws Exception {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void create(User u) throws Exception {
        String sql = "INSERT INTO users(full_name, username, password_hash, role) VALUES(?,?,?,?)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.fullName);
            ps.setString(2, u.username);
            ps.setString(3, u.passwordHash);
            ps.setString(4, u.role);
            ps.executeUpdate();
        }
    }

    @Override
    public User findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, full_name, username, password_hash, role FROM users WHERE username = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                User u = new User();
                u.userId = rs.getInt("user_id");
                u.fullName = rs.getString("full_name");
                u.username = rs.getString("username");
                u.passwordHash = rs.getString("password_hash");
                u.role = rs.getString("role");
                return u;
            }
        }
    }
}