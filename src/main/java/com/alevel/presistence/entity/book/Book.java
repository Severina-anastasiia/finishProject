package com.alevel.presistence.entity.book;

import com.alevel.presistence.entity.AbstractEntity;
import com.alevel.presistence.entity.user.Personal;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends AbstractEntity {

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Personal personal;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String description;

    public Book(){
        super();
    }
}
