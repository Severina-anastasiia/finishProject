package com.alevel.presistence.entity.book;

import com.alevel.presistence.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "reactions")
public class Reaction extends AbstractEntity {

    @Column(name = "PERSONAL_ID", nullable = false)
    private Integer personalId;

    @Column(name = "BOOK_ID", nullable = false)
    private Integer bookId;

    @Column(name = "LIKE_BOOK", nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean like;

    public Reaction(){
        super();
        this.like = false;
    }
}
