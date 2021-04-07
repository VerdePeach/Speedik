package com.in726.app.database.service;

import com.in726.app.database.HibernateUtil;
import com.in726.app.database.dao.LetterDao;
import com.in726.app.enums.Period;
import com.in726.app.model.sub_functional_model.Letter;
import com.in726.app.model.sub_functional_model.Link;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Realisation of LetterDao interface.
 * Service for saving, updating, getting and deleting letters from database.
 */
public class LetterService implements LetterDao {
    /**
     * Adds new letter to database.
     *
     * @param letter letter
     */
    @Override
    public void save(Letter letter) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.save(letter);
            transaction.commit();
        }
    }

    /**
     * Counts current amount of letters in database.
     *
     * @return amount of letters
     */
    @Override
    public long getLettersCount() {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession();) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("select count(*) from Letter");
            long resultList = (long) query.getSingleResult();
            transaction.commit();
            return resultList;
        }
    }

    /**
     * fetches letters count by period.
     *
     * @param period period
     * @return count of letters
     */
    @Override
    public long getLettersCountByPeriod(String period) {
        try (var session = HibernateUtil.startSession();) {
            var transaction = session.beginTransaction();
            Query query = session.createQuery("select count(*) from Letter where created between ?1 and ?2");
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
}
