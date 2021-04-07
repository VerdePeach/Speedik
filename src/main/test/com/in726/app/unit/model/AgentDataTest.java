package com.in726.app.unit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.model.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentDataTest {
    @Test
    public void settingAndGettingPropertiesPositive() {

        var id = 1;
        var agentVersion = "1.0.1";
        var cpus = new ArrayList<Cpu>();
        var disk = new ArrayList<Disk>();
        var memory = new Memory();
        var timeAdd = new Date();
        var timeBoot = new Date();
        var agent = new Agent();


        var agentData = new AgentData();

        agentData.setId(id);
        agentData.setMemory(memory);
        agentData.setDisks(disk);
        agentData.setCpus(cpus);
        agentData.setAgent(agent);
        agentData.setTimeAdd(timeAdd);
        agentData.setBootTime(timeBoot);
        agentData.setAgentVersion(agentVersion);

        Assert.assertTrue(id == agentData.getId());
        Assert.assertEquals(agentVersion, agentData.getAgentVersion());
        Assert.assertEquals(cpus, agentData.getCpus());
        Assert.assertEquals(disk, agentData.getCpus());
        Assert.assertEquals(memory, agentData.getMemory());
        Assert.assertEquals(timeAdd, agentData.getTimeAdd());
        Assert.assertEquals(timeBoot, agentData.getBootTime());
        Assert.assertEquals(agent, agentData.getAgent());
        Assert.assertEquals(agent, agentData.getAgent());
    }
}
