package com.in726.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Model of agent entity.
 */
@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String host;

    @Column(name = "public_key", unique = true)
    private String publicKey;

    @JsonIgnore
    private String secretKey;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "agent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AgentData> agentData;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActive;
}
