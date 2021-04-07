package com.in726.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Model of Disk entity.
 */
@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(exclude = "agentData")
public class Disk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String origin;

    private long free;

    private long total;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private AgentData agentData;
}
