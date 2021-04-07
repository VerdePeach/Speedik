package com.in726.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.enums.TariffPlan;
import com.in726.app.enums.Roles;
import com.in726.app.model.sub_functional_model.Letter;
import com.in726.app.model.sub_functional_model.Link;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Model of User entity.
 */
@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private YesNoStatus confirm;

    @Enumerated(EnumType.STRING)
    private TariffPlan tariff;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Agent> agents;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Link> links;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Letter> letters;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActive;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Dashboard dashboard;
}
