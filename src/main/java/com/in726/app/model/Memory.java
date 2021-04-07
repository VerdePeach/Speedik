package com.in726.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Model of Memory entity.
 */
@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(exclude = "agentData")
@ToString(exclude = "agentData")
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long wired;
    private long active;
    private long free;
    private long total;
    private long inactive;

    @JsonIgnore
    @OneToOne
    private AgentData agentData;

}
