package com.alevel.facade;

import com.alevel.web.data.request.ChapterData;
import com.alevel.web.data.response.PageData;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ChapterFacade {

    void create(ChapterData data);
    void update(ChapterData data, Integer id);
    void delete(Integer id);
    void addChapter(Integer bookId);
    void uploadFile(MultipartFile file, Integer bookId);
    PageData<ChapterData> findAll(WebRequest request);
}
