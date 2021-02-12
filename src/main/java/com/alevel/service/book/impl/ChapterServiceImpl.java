package com.alevel.service.book.impl;

import com.alevel.presistence.data.PersistenceRequestData;
import com.alevel.presistence.entity.book.Book;
import com.alevel.presistence.entity.book.Chapter;
import com.alevel.presistence.repository.book.BookRepository;
import com.alevel.presistence.repository.book.ChapterRepository;
import com.alevel.presistence.repository.user.PersonalRepository;
import com.alevel.service.book.BookService;
import com.alevel.service.book.ChapterService;
import com.alevel.util.SecurityUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final BookRepository bookRepository;
    private final BookService bookService;
    private final ChapterRepository chapterRepository;
    private final PersonalRepository personalRepository;

    public ChapterServiceImpl(BookRepository bookRepository, BookService bookService, ChapterRepository chapterRepository, PersonalRepository personalRepository) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.chapterRepository = chapterRepository;
        this.personalRepository = personalRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public Page<Chapter> findAll(PersistenceRequestData persistenceRequestData) {
        Book book = bookRepository.findByPersonal(personalRepository.findByEmail(SecurityUtil.getUsername()));
        Sort sort = persistenceRequestData.getOrder().equals("desc") ?
                Sort.by(Sort.Order.desc(persistenceRequestData.getSort())) :
                Sort.by(Sort.Order.asc((persistenceRequestData.getSort())));
        if(persistenceRequestData.isOwner()){
            return chapterRepository.findAllByBookId(book.getId(), PageRequest.of(persistenceRequestData.getPage()-1, persistenceRequestData.getSize(), sort));
        } else {
            throw new RuntimeException("this book has not such chapter");
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public Chapter findById(Integer id) {
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        existChapter(chapter);
        return chapter;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void create(Chapter chapter) {
        Book book = bookRepository.findByPersonal(personalRepository.findByEmail(SecurityUtil.getUsername()));
        chapter.setBookId(book.getId());
        chapterRepository.save(chapter);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void update(Chapter chapter) {
        Book book = bookRepository.findByPersonal(personalRepository.findByEmail(SecurityUtil.getUsername()));
        Chapter current = chapterRepository.findById(chapter.getId()).orElse(null);
        validChapter(current, book.getId());
        current.setTitle(chapter.getTitle());
        current.setText(chapter.getText());
        chapterRepository.save(current);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void delete(Integer id) {
        Book book = bookRepository.findByPersonal(personalRepository.findByEmail(SecurityUtil.getUsername()));
        Chapter chapter = chapterRepository.findById(id).orElse(null);
        validChapter(chapter, book.getId());
        chapterRepository.delete(chapter);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void deleteByBookId(Integer bookId) {
        List<Chapter> chapters = chapterRepository.findAllByBookId(bookId);
        if(CollectionUtils.isNotEmpty(chapters)){
            chapterRepository.deleteAll(chapters);
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void addChapter(Integer bookId) {
        Chapter chapter = chapterRepository.findByBookId(bookId);
        if(chapter == null){
            chapter = new Chapter();
            chapter.setBookId(bookId);
        }
        chapterRepository.save(chapter);
    }

    @Override
    public void uploadFile(MultipartFile file, Integer bookId) {
        bookService.uploadFile(file, bookId);
    }

    private void existChapter(Chapter chapter){
        if(chapter == null){
            throw new RuntimeException("chapter not found");
        }
    }

    private void validChapter(Chapter chapter, Integer bookId){
        existChapter(chapter);
        if(!chapter.getBookId().equals(bookId)){
            throw new RuntimeException("you are not an owner");
        }
        if(StringUtils.isBlank(chapter.getText()) || StringUtils.isBlank(chapter.getTitle())){
            throw new RuntimeException("text or title cannot be empty");
        }
    }
}
