package com.in726.app.database.service;

import com.in726.app.database.HibernateUtil;
import com.in726.app.database.dao.AgentDataDao;
import com.in726.app.model.AgentData;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Realisation of AgentDataDao interface.
 * Service for CRUD operations with agent data.
 */
public class AgentDataService implements AgentDataDao {

    /**
     * Searches agent data by agent id
     * and date for old data by tariff
     *
     * @param agentId        agent id
     * @param dateTill date till delete
     * @return agent list
     */
    @Override
    public List<AgentData> findAgentDataByAgentIdAndDays(int agentId, Date dateTill) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from AgentData where agent_id = ?1 and time_add <= ?2");
            query.setParameter(1, agentId);
            query.setParameter(2, dateTill, TemporalType.DATE);
            var agentDataList = query.getResultList();
            transaction.commit();
            return agentDataList;
        }
    }

    /**
     * Deletes agent data.
     *
     * @param agentData agent data
     */
    @Override
    public void deleteAgentData(AgentData agentData) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.delete(agentData);
            transaction.commit();
        }
    }

    /**
     * Adds new agent data.
     *
     * @param agentData agent data
     */
    @Override
    public void save(AgentData agentData) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.save(agentData);
            transaction.commit();
        }
    }

    //    @Override
//    public void deleteAgentDataByAgentIdAndDays(int agentId, Date dateTillDelete) {
//        Transaction transaction = null;
//        try(var session = HibernateUtil.startSession()) {
//            transaction = session.beginTransaction();
//            Query query = session.createQuery("delete AgentData where agent_id = ?1 and time_add < ?2");
//            query.setParameter(1, agentId);
//            query.setParameter(2, dateTillDelete, TemporalType.DATE);
//            query.executeUpdate();
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null)
//                transaction.rollback();
//            e.printStackTrace();
//        }
//    }
}
