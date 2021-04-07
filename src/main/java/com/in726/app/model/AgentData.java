package com.in726.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Model of AgentData entity.
 */
@Data
@NoArgsConstructor
@Entity
//@Table(name = "agent_data")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(exclude = {"memory", "cpu", "agent", "disk"})
@ToString(exclude = {"memory", "cpu", "agent", "disk"})
public class AgentData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "agentData")
    private List<Cpu> cpus;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentData")
    private List<Disk> disks;

    @OneToOne(cascade = CascadeType.ALL)
    private Memory memory;

    @Column(name = "time_add")
    private Date timeAdd;

    @Column(name = "boot_time")
    private Date bootTime;

    @Column(name = "agent_version")
    private String agentVersion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;


}
