package com.alevel.facade.impl;

import com.alevel.facade.ChapterFacade;
import com.alevel.presistence.data.PersistenceRequestData;
import com.alevel.presistence.entity.book.Book;
import com.alevel.presistence.entity.book.Chapter;
import com.alevel.service.book.ChapterService;
import com.alevel.web.data.request.BookData;
import com.alevel.web.data.request.ChapterData;
import com.alevel.web.data.response.PageData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterFacadeImpl implements ChapterFacade {

    private final ChapterService chapterService;

    public ChapterFacadeImpl(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @Override
    public void create(ChapterData data) {
        Chapter chapter = new Chapter();
        chapter.setBookId(data.getBookId());
        chapter.setTitle(data.getTitle());
        chapter.setText(data.getText());
        chapterService.create(chapter);
    }

    @Override
    public void update(ChapterData data, Integer id) {
        Chapter chapter = chapterService.findById(id);
        chapter.setText(data.getText());
        chapter.setTitle(data.getTitle());
        chapterService.update(chapter);
    }

    @Override
    public void delete(Integer id) {
        chapterService.delete(id);
    }

    @Override
    public void addChapter(Integer bookId) {
        chapterService.addChapter(bookId);
    }

    @Override
    public void uploadFile(MultipartFile file, Integer bookId) {
        chapterService.uploadFile(file, bookId);
    }

    @Override
    public PageData<ChapterData> findAll(WebRequest request) {
        PersistenceRequestData persistenceRequestData = new PersistenceRequestData(request);
        Page<Chapter> page = chapterService.findAll(persistenceRequestData);
        PageData<ChapterData> data = new PageData<>();
        data.setCurrentPage(page.getNumber());
        data.setPageSize(page.getNumber());
        data.setTotalElement(page.getTotalPages());
        data.setTotalPages(page.getTotalPages());
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<ChapterData> list = page.getContent().stream().map(ChapterData::new).collect(Collectors.toList());
            data.setItems(list);
        }
        return data;
    }
}
