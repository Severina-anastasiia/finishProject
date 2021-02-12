package com.alevel.facade;

import com.alevel.web.data.request.BookData;
import com.alevel.web.data.response.PageData;
import com.alevel.web.data.response.BookResponseData;
import com.alevel.web.data.response.personal.PersonalDashboardChartData;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

public interface BookFacade {
    void create(BookData data);
    void update(BookData data, Integer id);
    void delete(Integer id);
    PageData<BookData> findAll(WebRequest request);
    BookResponseData findById(Integer id);
    void like(Integer id);
    void dislike(Integer id);
    void uploadFile(MultipartFile file, Integer postId);
    void addChapter(BookData data);
    PersonalDashboardChartData generatePersonalDashboardChartData();
}
