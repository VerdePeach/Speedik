package com.in726.app.parser.json_model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Model of MemoryJson data that agents send to server.
 */
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class MemoryJson {
    private long wired;
    private long free;
    private long active;
    private long inactive;
    private long total;
}
