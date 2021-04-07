package com.in726.app.database.service;

import com.in726.app.database.HibernateUtil;
import com.in726.app.database.dao.CheckLinkDao;
import com.in726.app.enums.Period;
import com.in726.app.model.sub_functional_model.CheckLink;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Realisation of CheckLinkDao interface.
 * Service for CRUD operations with checks of links.
 */
public class CheckLinkService implements CheckLinkDao {
    /**
     * Adds new check to database.
     *
     * @param checkLink check of links
     */
    @Override
    public void save(CheckLink checkLink) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.save(checkLink);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts amount of done checks.
     *
     * @return amount of checks
     */
    @Override
    public long getLinkChecksCount() {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession();) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("select count(*) from CheckLink");
            long resultList = (long) query.getSingleResult();
            transaction.commit();
            return resultList;
        }
    }

    /**
     * fetches link checks by period.
     *
     * @param period period
     * @return count of checks.
     */
    @Override
    public long getLinkChecksCountByPeriod(String period) {
        try (var session = HibernateUtil.startSession();) {
            var transaction = session.beginTransaction();
            Query query = session.createQuery("select count(*) from CheckLink where checkDate between ?1 and ?2");
            if (period.equals(Period.WEEK.name().toUpperCase())) {
                query.setParameter(1, new Date(new Date().getTime() - (604800000 * 2)), TemporalType.DATE);
                query.setParameter(2, new Date(new Date().getTime() - 604800000), TemporalType.DATE);
            } else if (period.equals(Period.DAY.name().toUpperCase())) {
                query.setParameter(1, new Date(new Date().getTime() - (86400000 * 2)), TemporalType.DATE);
                query.setParameter(2, new Date(new Date().getTime() - 86400000), TemporalType.DATE);
            } else if (period.equals(Period.NOWWEEK.name().toUpperCase())) {
                query.setParameter(1, new Date(new Date().getTime() - 604800000), TemporalType.DATE);
                query.setParameter(2, new Date(), TemporalType.DATE);
            } else if (period.equals(Period.NOWDAY.name().toUpperCase())) {
                query.setParameter(1, new Date(new Date().getTime() - 86400000), TemporalType.DATE);
                query.setParameter(2, new Date(), TemporalType.DATE);
            }

            long resultList = (long) query.getSingleResult();
            transaction.commit();
            return resultList;
        }
    }

    //    @Override
//    public void deleteByLinkId(int linkId) {
//       //TODO: write logic.
//    }

    /**
     * Searches checks by link id.
     *
     * @param linkId link id
     * @return list of checks
     */
    @Override
    public List<CheckLink> getChecksByLinkId(long linkId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from CheckLink where link_id = ?1");
            query.setParameter(1, linkId);
            List<CheckLink> resultList = query.getResultList();

            transaction.commit();
            return resultList;
        }
    }

    /**
     * fetches last check by link id.
     *
     * @param linkId link id
     * @return check
     */
    @Override
    public CheckLink getLastCheckByLinkId(long linkId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from CheckLink where checkDate = (select max(checkDate) from CheckLink where link_id = ?1) and link_id = ?1");
            query.setParameter(1, linkId);
            List<CheckLink> resultList = query.getResultList();

            transaction.commit();
            return resultList.size() > 0 ? resultList.get(0) : null;
        }
    }

    /**
     * fetches week checks bt link id.
     *
     * @param linkId link id
     * @return list of checks.
     */
    @Override
    public List<CheckLink> getWeekChecksByLinkId(long linkId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from CheckLink where link_id = ?1 and checkDate >= ?2");
            query.setParameter(1, linkId);
            query.setParameter(2, new Date(new Date().getTime() - 604800000), TemporalType.DATE);
            List<CheckLink> resultList = query.getResultList();

            transaction.commit();
            return resultList;
        }
    }
}
