package com.in726.app.parser.json_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Model of AgentJson data that agents send to server.
 */
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentJson {
    private String host;
    private Date at;
    private Date boot_time;
    private String public_key;
    private String agent_version;

    private List<CpuJson> cpu;
    private List<DiskJson> disks;
    private List<Double> load;
    private MemoryJson memory;
    private List<NetworkJson> network;
}
