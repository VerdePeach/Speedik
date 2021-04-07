package com.in726.app.parser.json_model;

import lombok.Data;

import java.util.List;

/**
 * Model of NetworkJson data that agents send to server.
 */
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkJson {
    private String name;
    private long in;
    private long out;
}
