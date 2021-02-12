package com.alevel.web.data.response;

import com.alevel.presistence.entity.book.Book;
import com.alevel.web.data.request.BookData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class BookResponseData extends BookData {

    private Map<Integer, String> chapters;
    private Map<Integer, String> likes;
    private Map<Integer, String> dislikes;

    public BookResponseData(Book book){
        super();
        this.chapters = new HashMap<>();
        this.likes = new HashMap<>();
        this.dislikes = new HashMap<>();
    }
}
