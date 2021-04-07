package com.in726.app.unit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.model.AgentData;
import com.in726.app.model.Cpu;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

public class CpuTest {

    @Test
    public void settingAndGettingProperties() {

        var id = 1;
        var num = 0;
        var userLoad = 2;
        var systemLoad = 3;
        var idle = 5;
        var agentData = new AgentData();

        var cpu = new Cpu();
        cpu.setId(id);
        cpu.setIdle(idle);
        cpu.setSystemLoad(systemLoad);
        cpu.setUserLoad(userLoad);
        cpu.setNum(num);
        cpu.setAgentData(agentData);

        Assert.assertTrue(id == cpu.getId());
        Assert.assertTrue(num == cpu.getNum());
        Assert.assertTrue(idle == cpu.getIdle());
        Assert.assertTrue(systemLoad == cpu.getSystemLoad());
        Assert.assertTrue(userLoad == cpu.getUserLoad());
        Assert.assertEquals(agentData, cpu.getAgentData());
    }
}
