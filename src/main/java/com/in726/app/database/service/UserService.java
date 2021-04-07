package com.in726.app.database.service;

import com.in726.app.database.HibernateUtil;
import com.in726.app.database.dao.UserDao;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.model.Agent;
import com.in726.app.model.User;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Realisation of UserDao interface.
 * Service for saving, updating, getting and deleting users in database.
 */
public class UserService implements UserDao {

    /**
     * Saves new user to database.
     *
     * @param user new user.
     */
    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
    }

    /**
     * Updates information about user.
     *
     * @param user user that should be updated.
     */
    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        }
    }

    /**
     * Deletes user from database
     *
     * @param user user for deleting
     */
    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        }
    }

    /**
     * Searches all users in database
     *
     * @return List of users
     */
    @Override
    public List<User> getAll() {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            var userList = session.createQuery("from User", User.class).getResultList();
            transaction.commit();
            return userList;
        }
    }

    /**
     * Searches user by username.
     *
     * @param username username of user
     * @return user
     */
    @Override
    public User getUserByUsername(String username) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from User where username = ?1");
            query.setParameter(1, username);
            List<User> resultList = query.getResultList();
            transaction.commit();

            return resultList.isEmpty() ? null : resultList.get(0);
        }
    }

    /**
     * Searches user by user id
     *
     * @param userId id of the user
     * @return user
     */
    @Override
    public User getUserById(int userId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from User where id = ?1");
            query.setParameter(1, userId);
            List<User> resultList = query.getResultList();
            transaction.commit();

            return resultList.isEmpty() ? null : resultList.get(0);
        }
    }

    /**
     * Searches user by email.
     *
     * @param email email
     * @return user
     */
    @Override
    public User getUserByEmail(String email) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from User where email = ?1");
            query.setParameter(1, email);
            List<User> resultList = query.getResultList();
            transaction.commit();

            return resultList.isEmpty() ? null : resultList.get(0);
        }
    }

    /**
     * Updates user activity date.
     *
     * @param userId user id
     */
    @Override
    public void setUserActiveByUserId(int userId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update User set lastActive = CURRENT_TIMESTAMP() where id = ?1");
            query.setParameter(1, userId);
            query.executeUpdate();
            transaction.commit();
        }
    }

    /**
     * Searches active or inactive users
     * depends on userActive parameter
     * and set the date from which users are inactive
     *
     * @param date       date
     * @param userActive NO or YES
     * @return list of users
     */
    @Override
    public List<User> findUsersByActive(Date date, String userActive) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            String sing = ">=";
            if (userActive.toUpperCase().equals(YesNoStatus.NO.name().toUpperCase())) sing = "<=";
            transaction = session.beginTransaction();
            Query query = session.createQuery("from User where lastActive " + sing + " ?1");
            query.setParameter(1, date, TemporalType.DATE);
            List<User> userList = query.getResultList();
            transaction.commit();

            userList.forEach(u -> {
                u.setAgents(null);
                u.setPassword(null);
            });
            return userList;
        }
    }
}
