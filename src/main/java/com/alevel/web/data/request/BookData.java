package com.alevel.web.data.request;

import com.alevel.presistence.entity.book.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BookData {

    private Integer id;
    private String title;
    private String description;
    private Date created;

    public BookData(Book book){
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.created = book.getCreated();
    }
}
