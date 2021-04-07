package com.in726.app.parser.json_model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Model of CpuJson data that agents send to server.
 */
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class CpuJson {
    @JsonIgnore
    private int num;
    private double user;
    private double system;
    private double idle;
}
