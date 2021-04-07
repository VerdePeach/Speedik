package com.in726.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Model of Cpu Entity.
 */
@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(exclude = "agentData")
public class Cpu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int num;

    @Column(name = "user_load")
    private double userLoad;

    @Column(name = "system_load")
    private double systemLoad;

    private double idle;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private AgentData agentData;

}
