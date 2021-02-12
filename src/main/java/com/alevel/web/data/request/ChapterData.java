package com.alevel.web.data.request;

import com.alevel.presistence.entity.book.Book;
import com.alevel.presistence.entity.book.Chapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ChapterData {

    private Integer id;
    private Integer bookId;
    private String title;
    private String text;
    private Date created;

    public ChapterData(Chapter chapter){
        this.id = chapter.getId();
        this.bookId = chapter.getBookId();
        this.title = chapter.getTitle();
        this.text = chapter.getText();
        this.created = chapter.getCreated();
    }
}
