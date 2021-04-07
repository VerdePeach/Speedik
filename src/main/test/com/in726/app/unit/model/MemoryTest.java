package com.in726.app.unit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.model.AgentData;
import com.in726.app.model.Cpu;
import com.in726.app.model.Memory;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class MemoryTest {

    @Test
    public void gettingAndSettingProperties() {

        var id = 1;
        var wired = 2;
        var active = 3;
        var free = 4;
        var total = 5;
        var inactive = 6;
        var agentData = new AgentData();

        var memory = new Memory();

        memory.setId(id);
        memory.setWired(wired);
        memory.setActive(active);
        memory.setFree(free);
        memory.setTotal(total);
        memory.setInactive(inactive);
        memory.setAgentData(agentData);

        Assert.assertTrue(id == memory.getId());
        Assert.assertTrue(wired == memory.getWired());
        Assert.assertTrue(active == memory.getActive());
        Assert.assertTrue(inactive == memory.getInactive());
        Assert.assertTrue(free == memory.getFree());
        Assert.assertTrue(total == memory.getTotal());
        Assert.assertEquals(agentData, memory.getAgentData());
        Assert.assertEquals("Memory(id=1, wired=2, active=3, free=4, total=5, inactive=6)", memory.toString());
    }
}
