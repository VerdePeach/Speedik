package com.in726.app.database.dao;

import com.in726.app.model.AgentData;

import java.util.Date;
import java.util.List;

/**
 * AgentData DAO interface.
 */
public interface AgentDataDao {
    //    void deleteAgentDataByAgentIdAndDays(int agentId, Date dateTillDelete);

    void deleteAgentData(AgentData agentData);

    List<AgentData> findAgentDataByAgentIdAndDays(int agentId, Date dateTillDelete);

    void save(AgentData agentData);
}
