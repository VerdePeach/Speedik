package com.in726.app.model.sub_functional_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.enums.LinkStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Model of CheckLink entity.
 */
@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = {"link"})
@ToString(exclude = {"link"})
public class CheckLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int httpStatus;

    @Type(type = "yes_no")
    private boolean allWordsFind;

    @Enumerated(EnumType.STRING)
    private LinkStatus status;

    @CreationTimestamp
    private Date checkDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Link link;

    public CheckLink(int httpStatus, boolean allWordsFind, LinkStatus status, Link link) {
        this.httpStatus = httpStatus;
        this.allWordsFind = allWordsFind;
        this.status = status;
        this.link = link;
    }
}
