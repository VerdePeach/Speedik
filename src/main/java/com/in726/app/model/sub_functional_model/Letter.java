package com.in726.app.model.sub_functional_model;

import com.in726.app.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Model of Letter entity.
 */
@Data
@Entity
@NoArgsConstructor
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String subject;

    @Transient
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
}
