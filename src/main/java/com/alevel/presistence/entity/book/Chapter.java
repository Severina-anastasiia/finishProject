package com.alevel.presistence.entity.book;

import com.alevel.presistence.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "chapters1")
public class Chapter extends AbstractEntity {

    @Column(name = "BOOK_ID", nullable = false)
    private Integer bookId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "TEXT", nullable = false, columnDefinition = "TEXT")
    private String text;

    public Chapter(){
        super();
    }
}
