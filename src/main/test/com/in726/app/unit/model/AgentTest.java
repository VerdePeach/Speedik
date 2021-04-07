package com.in726.app.unit.model;

import com.in726.app.model.Agent;
import com.in726.app.model.AgentData;
import com.in726.app.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AgentTest {

    @Test
    public void settingAndGettingPropertiesPositive() {
        var id = 1;
        var user = new User();
        var lastActive = new Date();
        var agentData = new ArrayList<AgentData>();
        var secretKey = "Hello-World";
        var publicKey = "Hi-World";
        var host = "Antanatarivu/45.dfw/24";

        var agent = new Agent();
        agent.setId(id);
        agent.setUser(user);
        agent.setLastActive(lastActive);
        agent.setAgentData(agentData);
        agent.setSecretKey(secretKey);
        agent.setPublicKey(publicKey);
        agent.setHost(host);

        Assert.assertTrue(id == agent.getId());
        Assert.assertEquals(user, agent.getUser());
        Assert.assertEquals(lastActive, agent.getLastActive());
        Assert.assertEquals(agentData, agent.getAgentData());
        Assert.assertEquals(secretKey, agent.getSecretKey());
        Assert.assertEquals(publicKey, agent.getPublicKey());
        Assert.assertEquals(host, agent.getHost());
    }
}
