package com.in726.app.convertor;

import com.in726.app.model.*;
import com.in726.app.parser.json_model.AgentJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for converting received data from agents to Agent model.
 */
public class AgentConvertor {

    /**
     * Convert Agent`s data model (AgentJson) to Agent model - fit form for database.
     *
     * @param agentJson Agent`s data model
     * @return agent
     */
    public Agent convertAgentJsonToAgent(AgentJson agentJson) {

        Agent agent = new Agent();
        agent.setHost(agentJson.getHost());
        agent.setPublicKey(agentJson.getPublic_key());

        AgentData agentData = new AgentData();
        agentData.setAgent(agent);
        agentData.setAgentVersion(agentJson.getAgent_version());
        agentData.setBootTime(agentJson.getBoot_time());

        agentData.setTimeAdd(agentJson.getAt());

        List<Cpu> cpus = new ArrayList<>();
        agentJson.getCpu().forEach(c -> {
            Cpu cpu = new Cpu();
            cpu.setIdle(c.getIdle());
            cpu.setNum(c.getNum());
            cpu.setSystemLoad(c.getSystem());
            cpu.setUserLoad(c.getUser());
            cpu.setAgentData(agentData);
            cpus.add(cpu);
        });
        agentData.setCpus(cpus);

        List<Disk> disks = new ArrayList<>();
        agentJson.getDisks().forEach(d -> {
            Disk disk = new Disk();
            disk.setFree(d.getFree());
            disk.setOrigin(d.getOrigin());
            disk.setTotal(d.getTotal());
            disk.setAgentData(agentData);
            disks.add(disk);
        });
        agentData.setDisks(disks);

        Memory memory = new Memory();
        memory.setActive(agentJson.getMemory().getActive());
        memory.setFree(agentJson.getMemory().getFree());
        memory.setInactive(agentJson.getMemory().getInactive());
        memory.setTotal(agentJson.getMemory().getTotal());
        memory.setWired(agentJson.getMemory().getWired());
        memory.setAgentData(agentData);
        agentData.setMemory(memory);

        var agentDataList = new ArrayList<AgentData>();
        agentDataList.add(agentData);
        agent.setAgentData(agentDataList);

        return agent;
    }
}
