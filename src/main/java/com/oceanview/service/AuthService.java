package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserDAOImpl;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

public class AuthService {
    private final UserDAO userDAO = new UserDAOImpl();

    public void register(String fullName, String username, String password, String role) throws Exception {
        if (fullName == null || fullName.trim().isEmpty()) throw new IllegalArgumentException("Full name required");
        if (username == null || username.trim().isEmpty()) throw new IllegalArgumentException("Username required");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password too short");
        if (role == null || role.trim().isEmpty()) throw new IllegalArgumentException("Role required");

        if (userDAO.usernameExists(username.trim())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User u = new User();
        u.fullName = fullName.trim();
        u.username = username.trim();
        u.passwordHash = PasswordUtil.sha256(password);
        u.role = role.trim().toUpperCase();

        userDAO.create(u);
    }

    public User login(String username, String password) throws Exception {
        if (username == null || password == null) return null;
        User u = userDAO.findByUsername(username.trim());
        if (u == null) return null;
        String hash = PasswordUtil.sha256(password);
        if (!hash.equals(u.passwordHash)) return null;
        return u;
    }
}