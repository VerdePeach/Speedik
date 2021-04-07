package com.in726.app.unit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.model.AgentData;
import com.in726.app.model.Disk;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

public class DiskTest {

    @Test
    public void gettingAndSetting() {
        var id = 1;
        var origin = "C:/home";
        var free = 3;
        var total = 4;
        var agentData = new AgentData();

        var disk = new Disk();
        disk.setId(id);
        disk.setTotal(total);
        disk.setOrigin(origin);
        disk.setFree(free);
        disk.setAgentData(agentData);

        Assert.assertEquals(agentData, disk.getAgentData());
        Assert.assertEquals(origin, disk.getOrigin());
        Assert.assertTrue(id == disk.getId());
        Assert.assertTrue(free == disk.getFree());
        Assert.assertTrue(total == disk.getTotal());

    }
}
