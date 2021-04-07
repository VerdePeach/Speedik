package com.in726.app.parser.json_model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Model of DiskJson data that agents send to server.
 */
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class DiskJson {
    private String origin;
    private long free;
    private long total;
}
