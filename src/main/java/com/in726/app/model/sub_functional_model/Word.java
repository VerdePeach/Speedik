package com.in726.app.model.sub_functional_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Model of Word entity.
 */
@Data
@NoArgsConstructor
@Entity
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Link link;
}
