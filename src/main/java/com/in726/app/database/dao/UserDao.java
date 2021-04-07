package com.in726.app.database.dao;

import com.in726.app.model.User;

import java.util.Date;
import java.util.List;

/**
 * User DAO interface.
 */
public interface UserDao {
    void save(User user);

    void update(User user);

    void delete(User user);

    List<User> getAll();

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User getUserById(int userId);

    void setUserActiveByUserId(int userId);

    List<User> findUsersByActive(Date date, String userActive);
}
