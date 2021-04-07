package com.in726.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.model.sub_functional_model.Link;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = {"links", "user"})
@ToString(exclude = {"links", "user"})
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String url;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dashboard", fetch = FetchType.EAGER)
    private List<Link> links;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
