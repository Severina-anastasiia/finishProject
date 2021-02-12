package com.alevel.service.book;

import com.alevel.presistence.entity.book.Book;
import com.alevel.service.CrudService;
import org.springframework.web.multipart.MultipartFile;

public interface BookService extends CrudService<Book> {

    void like(Integer id);
    void dislike(Integer id);
    void uploadFile(MultipartFile file, Integer bookId);
    void addChapter(Integer id);
}
