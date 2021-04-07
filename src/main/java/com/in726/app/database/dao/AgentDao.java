package com.in726.app.database.dao;

import com.in726.app.model.Agent;

import java.util.Date;
import java.util.List;

/**
 * Agent DAO interface.
 */
public interface AgentDao {
    void createAgent(Agent agent);

    void updateAgent(Agent agent);

    void deleteAgent(Agent agent);

    Agent findByAgentId(int agentId);

    //    void deleteAgentById(int id);
    //    List<Agent> findAllAgents();
    //    Agent findAgentById(int id);
    Agent findAgentByPublicKey(String key);

    //    List<Agent> findAgentsByUser(User user);
    List<Agent> findAgentsByUserId(int userId);

    long amountOfServersForUserByUserId(int userId);

//    void setActiveDate(int agentId);

    List<Agent> findAgentsByActive(Date date, String agentActive);
}
