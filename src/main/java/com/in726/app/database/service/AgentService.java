package com.in726.app.database.service;

import com.in726.app.database.HibernateUtil;
import com.in726.app.database.dao.AgentDao;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.model.Agent;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Realisation of AgentDao interface.
 * Service for saving, updating, getting and deleting agents in database.
 */
public class AgentService implements AgentDao {

    /**
     * Creates new agent.
     *
     * @param agent agent that should be created.
     */
    @Override
    public void createAgent(Agent agent) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession();) {
            transaction = session.beginTransaction();
            session.save(agent);
            transaction.commit();
        }
    }

    /**
     * Updates information about the agent.
     *
     * @param agent agent that should be edited
     */
    @Override
    public void updateAgent(Agent agent) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.update(agent);
            transaction.commit();
        }
    }

    /**
     * Deletes agent from database.
     *
     * @param agent agent that should be deleted
     */
    @Override
    public void deleteAgent(Agent agent) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            session.delete(agent);
            transaction.commit();
        }
    }

    /**
     * fetches agent by id
     * @param agentId agent id
     * @return agent
     */
    @Override
    public Agent findByAgentId(int agentId) {
        try (var session = HibernateUtil.startSession()) {
            var transaction = session.beginTransaction();
            Query query = session.createQuery("from Agent where id = ?1");
            query.setParameter(1, agentId);
            var agent = (Agent) query.getSingleResult();
            transaction.commit();
            return agent;
        }
    }
//
//    @Override
//    public List<Agent> findAllAgents() {
//        return null;
//    }
//
//    @Override
//    public Agent findAgentById(int id) {
//        return null;
//    }
//
//    @Override
//    public List<Agent> findAgentsByUser(User user) {
//        return null;
//    }

    /**
     * Searches all agents of user by user id.
     *
     * @param userId user id.
     * @return List of agents
     */
    @Override
    public List<Agent> findAgentsByUserId(int userId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Agent where user_id = ?1");
            query.setParameter(1, userId);
            List<Agent> resultList = query.getResultList();
            transaction.commit();
            return resultList.isEmpty() ? null : resultList;
        }
    }

    /**
     * Searches agent by public key.
     *
     * @param key public key.
     * @return agent.
     */
    @Override
    public Agent findAgentByPublicKey(String key) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Agent where public_key = ?1");
            query.setParameter(1, key);
            List<Agent> resultList = query.getResultList();
            transaction.commit();

            return resultList.isEmpty() ? null : resultList.get(0);
        }
    }

    /**
     * Counts amount of servers for user by user id.
     *
     * @param userId user id
     * @return number of servers.
     */
    @Override
    public long amountOfServersForUserByUserId(int userId) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("select count(*) from Agent where user_id = ?1");
            query.setParameter(1, userId);
            long resultList = (long) query.getSingleResult();
            transaction.commit();

            return resultList;
        }
    }

    /**
     * Updates last active of agent.
     *
     * @param agentId agent id
     */
//    @Override
//    public void setActiveDate(int agentId) {
//        Transaction transaction = null;
//        try (var session = HibernateUtil.startSession()) {
//            transaction = session.beginTransaction();
//            Query query = session.createQuery("update Agent set lastActive = CURRENT_TIMESTAMP() where agent_id = ?1");
//            query.setParameter(1, agentId);
//            query.executeUpdate();
//            transaction.commit();
//        }
//    }

    /**
     * Searches agents by activity and date.
     *
     * @param date        date
     * @param agentActive NO or YES
     * @return list of agents
     */
    @Override
    public List<Agent> findAgentsByActive(Date date, String agentActive) {
        Transaction transaction = null;
        try (var session = HibernateUtil.startSession()) {
            String sing = ">=";
            if (agentActive.toUpperCase().equals(YesNoStatus.NO.name().toUpperCase())) sing = "<=";
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Agent where lastActive " + sing + " ?1");
            query.setParameter(1, date, TemporalType.DATE);
            List<Agent> agentList = query.getResultList();
            transaction.commit();

            agentList.forEach(a -> a.setAgentData(null));
            return agentList;
        }
    }
}
