package com.alevel.service.book;

import com.alevel.presistence.entity.book.Chapter;
import com.alevel.service.CrudService;
import org.springframework.web.multipart.MultipartFile;

public interface ChapterService extends CrudService<Chapter> {

    void deleteByBookId(Integer bookId);
    void addChapter(Integer bookId);
    void uploadFile(MultipartFile file, Integer bookId);
}
