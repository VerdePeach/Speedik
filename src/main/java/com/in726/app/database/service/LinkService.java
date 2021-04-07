package com.in726.app.database.service;

import com.in726.app.database.HibernateUtil;
import com.in726.app.database.dao.LinkDao;
import com.in726.app.model.sub_functional_model.Link;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

/**
 * Realisation of LinkDao interface.
 * Service for saving, updating, getting and deleting links from database.
 */
public class LinkService implements LinkDao {
    /**
     * Adds new link to database.
     *
     * @param link new link
     */
    @Override
    public void addLink(Link link) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.save(link);
            transaction.commit();
        }
    }

    /**
     * Searches amount of links by user id.
     *
     * @param userId user id
     * @return amount of links
     */
    @Override
    public long amountOfLinksForUserByUserId(int userId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession();) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("select count(*) from Link where user_id = ?1");
            query.setParameter(1, userId);
            long resultList = (long) query.getSingleResult();

            transaction.commit();
            return resultList;
        }
    }

    /**
     * Searches user's links by user id
     *
     * @param userId user id
     * @return list of links
     */
    @Override
    public List<Link> findLinksByUserId(int userId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession();) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Link where user_id = ?1");
            query.setParameter(1, userId);
            List<Link> resultList = query.getResultList();

            transaction.commit();
            return resultList;
        }
    }

    /**
     * fetches links by dashboard id
     *
     * @param dashId dashboard id
     * @return list of links
     */
    @Override
    public List<Link> findLinksByDashboardId(int dashId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession();) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Link where dashboard_id = ?1");
            query.setParameter(1, dashId);
            List<Link> resultList = query.getResultList();

            transaction.commit();
            return resultList;
        }
    }

    /**
     * Searches last added link by user id.
     *
     * @param userId user id
     * @return link
     */
    @Override
    public Link findLastAddedLinkByUserId(int userId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession();) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Link where user_id = ?1 " +
                    "and created = (select max(created) from Link)");
            query.setParameter(1, userId);
            var link = (Link) query.getResultList().get(0);

            transaction.commit();
            return link;
        }
    }

    /**
     * Updates link.
     *
     * @param link updated link
     */
    @Override
    public void updateLink(Link link) {
        try (var session = HibernateUtil.startSession();) {
            var transaction = session.beginTransaction();
            session.update(link);
            transaction.commit();
        }
    }
//
//    @Override
//    public void deleteLink(Link link) {
//
//    }
}
