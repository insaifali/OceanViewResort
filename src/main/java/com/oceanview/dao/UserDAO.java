package com.oceanview.dao;

import com.oceanview.model.User;

public interface UserDAO {
    boolean usernameExists(String username) throws Exception;
    void create(User u) throws Exception;
    User findByUsername(String username) throws Exception;
}