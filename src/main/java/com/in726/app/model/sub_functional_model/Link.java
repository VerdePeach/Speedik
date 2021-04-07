package com.in726.app.model.sub_functional_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.in726.app.model.Dashboard;
import com.in726.app.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Model of Link entity.
 */
@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = {"checks", "words"})
@ToString(exclude = {"checks", "words"})
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String url;
    private long secondsToCheck;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    //TODO: load checks by hand. because check how we load checks for check link table on front.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "link")
    private List<CheckLink> checks;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "link_id")
    private List<Word> words;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Dashboard dashboard;
}
