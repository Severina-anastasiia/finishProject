package com.alevel.facade.impl;

import com.alevel.facade.BookFacade;
import com.alevel.presistence.data.PersistenceRequestData;
import com.alevel.presistence.entity.book.Book;
import com.alevel.presistence.entity.book.Reaction;
import com.alevel.presistence.entity.user.Personal;
import com.alevel.service.book.BookService;
import com.alevel.service.book.ReactionService;
import com.alevel.service.user.PersonalService;
import com.alevel.util.BookUtil;
import com.alevel.web.data.KeyValueData;
import com.alevel.web.data.request.BookData;
import com.alevel.web.data.response.PageData;
import com.alevel.web.data.response.BookResponseData;
import com.alevel.web.data.response.personal.PersonalDashboardChartData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookFacadeImpl implements BookFacade {

    private final BookService bookService;
    private final ReactionService reactionService;
    private final PersonalService personalService;

    public BookFacadeImpl(BookService postService, ReactionService reactionService, PersonalService personalService) {
        this.bookService = postService;
        this.reactionService = reactionService;
        this.personalService = personalService;
    }

    @Override
    public void create(BookData data) {
        Book book = new Book();
        book.setDescription(data.getDescription());
        book.setTitle(data.getTitle());
        bookService.create(book);
    }

    @Override
    public void update(BookData data, Integer id) {
        Book book = bookService.findById(id);
        book.setDescription(data.getDescription());
        book.setTitle(data.getTitle());
        bookService.update(book);
    }

    @Override
    public void delete(Integer id) {
        bookService.delete(id);
    }

    @Override
    public PageData<BookData> findAll(WebRequest request) {
        PersistenceRequestData persistenceRequestData = new PersistenceRequestData(request);
        Page<Book> page = bookService.findAll(persistenceRequestData);
        PageData<BookData> data = new PageData<>();
        data.setCurrentPage(page.getNumber());
        data.setPageSize(page.getNumber());
        data.setTotalElement(page.getTotalPages());
        data.setTotalPages(page.getTotalPages());
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<BookData> list = page.getContent().stream().map(BookData::new).collect(Collectors.toList());
            data.setItems(list);
        }
        return data;
    }

    @Override
    public BookResponseData findById(Integer id) {
        Book book = bookService.findById(id);
        List<Reaction> likeReactionList = reactionService.findAllByBookIdAndLikeTrue(book.getId());
        List<Reaction> dislikeReactionList = reactionService.findAllByBookIdAndLikeFalse(book.getId());
        BookResponseData data = new BookResponseData(book);
        if (CollectionUtils.isNotEmpty(likeReactionList)) {
            List<Integer> ids = likeReactionList.stream().map(Reaction::getPersonalId).collect(Collectors.toList());
            generateBookResponseData(data, ids, null);
        }
        if (CollectionUtils.isNotEmpty(dislikeReactionList)) {
            List<Integer> ids = dislikeReactionList.stream().map(Reaction::getPersonalId).collect(Collectors.toList());
            generateBookResponseData(data, null, ids);
        }
        return data;
    }

    @Override
    public void like(Integer id) {
        bookService.like(id);
    }

    @Override
    public void dislike(Integer id) {
        bookService.dislike(id);
    }

    @Override
    public void uploadFile(MultipartFile file, Integer postId) {
        bookService.uploadFile(file, postId);
    }

    @Override
    public void addChapter(BookData data) {
        bookService.addChapter(data.getId());
    }

    @Override
    public PersonalDashboardChartData generatePersonalDashboardChartData() {
        PersonalDashboardChartData data = new PersonalDashboardChartData();
        Map<String, List<KeyValueData<Date, Long>>> chartDataMap = reactionService.generatedChartByBookReaction();
        if (MapUtils.isEmpty(chartDataMap)) {
            return data;
        }

        List<KeyValueData<Date, Long>> allPostData = chartDataMap.get(BookUtil.BOOK_ALL);
        List<KeyValueData<Date, Long>> likePostData = chartDataMap.get(BookUtil.LIKE_ALL);
        List<KeyValueData<Date, Long>> dislikePostData = chartDataMap.get(BookUtil.DISLIKE_ALL);

        List<Long> allPost = allPostData.stream().map(KeyValueData::getValue).collect(Collectors.toUnmodifiableList());
        List<Long> likePost = new ArrayList<>();
        List<Long> dislikePost = new ArrayList<>();

        List<Date> dates = allPostData.stream().map(KeyValueData::getKey).collect(Collectors.toUnmodifiableList());

        KeyValueData<Date, Long> keyValue;
        for (Date reactionDate : dates) {
            keyValue = likePostData.stream().filter(keyValueData -> keyValueData.getKey().getTime() == reactionDate.getTime()).findAny().orElse(null);
            if (keyValue == null) {
                likePost.add(0L);
            } else {
                likePost.add(keyValue.getValue());
            }
            keyValue = dislikePostData.stream().filter(keyValueData -> keyValueData.getKey().getTime() == reactionDate.getTime()).findAny().orElse(null);
            if (keyValue == null) {
                dislikePost.add(0L);
            } else {
                dislikePost.add(keyValue.getValue());
            }
        }

        data.setLabels(dates);
        data.setAllBook(allPost);
        data.setLikeBook(likePost);
        data.setDislikeBook(dislikePost);

        return data;
    }

    private void generateBookResponseData(BookResponseData data, List<Integer> likeIds, List<Integer> dislikeIds) {
        List<Personal> personals;
        Map<Integer, String> map;
        if (likeIds != null) {
            personals = personalService.findAllByListId(likeIds);
            map = personals.stream().collect(Collectors.toMap(Personal::getId, Personal::getNickName));
            data.setLikes(map);
        }
        if (dislikeIds != null) {
            personals = personalService.findAllByListId(dislikeIds);
            map = personals.stream().collect(Collectors.toMap(Personal::getId, Personal::getNickName));
            data.setDislikes(map);
        }
    }
}
