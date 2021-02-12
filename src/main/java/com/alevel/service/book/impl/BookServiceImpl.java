package com.alevel.service.book.impl;

import com.alevel.presistence.data.PersistenceRequestData;
import com.alevel.presistence.entity.book.Book;
import com.alevel.presistence.entity.user.Personal;
import com.alevel.presistence.repository.book.BookRepository;
import com.alevel.presistence.repository.user.PersonalRepository;
import com.alevel.service.book.BookService;
import com.alevel.service.book.ChapterService;
import com.alevel.service.book.ReactionService;
import com.alevel.util.SecurityUtil;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.alevel.util.WebRequestUtil.DEFAULT_ORDER_PARAM_VALUE;

@Service
public class BookServiceImpl implements BookService {

    private final String UPLOAD_DIR = "./uploads/";

    private final PersonalRepository personalRepository;
    private final BookRepository bookRepository;
    private final ReactionService reactionService;
    private final ChapterService chapterService;

    public BookServiceImpl(PersonalRepository personalRepository, BookRepository bookRepository, ReactionService reactionService, ChapterService chapterService) {
        this.personalRepository = personalRepository;
        this.bookRepository = bookRepository;
        this.reactionService = reactionService;
        this.chapterService = chapterService;
    }


    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public Page<Book> findAll(PersistenceRequestData persistenceRequestData) {
        Personal personal = personalRepository.findByEmail(SecurityUtil.getUsername());
        Sort sort = persistenceRequestData.getOrder().equals("desc") ?
                Sort.by(Sort.Order.desc(persistenceRequestData.getSort())) :
                Sort.by(Sort.Order.asc((persistenceRequestData.getSort())));
        if(persistenceRequestData.isOwner()){
            return bookRepository.findAllByPersonal(personal, PageRequest.of(persistenceRequestData.getPage()-1, persistenceRequestData.getSize(), sort));
        } else {
            return bookRepository.findAllByPersonalIsNot(personal, PageRequest.of(persistenceRequestData.getPage()-1, persistenceRequestData.getSize(), sort));
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public Book findById(Integer id) {
        Book book = bookRepository.findById(id).orElse(null);
        return book;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void create(Book book) {
        Personal personal = personalRepository.findByEmail(SecurityUtil.getUsername());
        book.setPersonal(personal);
        bookRepository.save(book);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void update(Book book) {
        Personal personal = personalRepository.findByEmail(SecurityUtil.getUsername());
        Book current = bookRepository.findById(book.getId()).orElse(null);
        validBook(book, personal.getId());
        current.setTitle(book.getTitle());
        current.setDescription(book.getDescription());
        bookRepository.save(current);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void delete(Integer id) {
        Personal personal = personalRepository.findByEmail(SecurityUtil.getUsername());
        Book book = bookRepository.findById(id).orElse(null);
        validBook(book, personal.getId());
        bookRepository.delete(book);
        reactionService.deleteByBookId(book.getId());
        chapterService.deleteByBookId(book.getId());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void like(Integer id) {
        reactionProcess(id, true);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void dislike(Integer id) {
        reactionProcess(id, false);
    }

    @Override
    public void uploadFile(MultipartFile file, Integer bookId) {
        String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
        try {
            File dir = new File(UPLOAD_DIR);
            if(!dir.exists()){
                dir.mkdirs();
            }
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void addChapter(Integer id) {
        Book book = bookRepository.findById(id).orElse(null);
        existBook(book);
        chapterService.addChapter(book.getId());
    }

    private void validBook(Book book, Integer personalId){
        existBook(book);
        if(!book.getPersonal().getId().equals(personalId)){
            throw new RuntimeException("you are not an owner");
        }
        if(StringUtils.isBlank(book.getDescription()) || StringUtils.isBlank(book.getTitle())){
            throw new RuntimeException("description or title cannot be empty");
        }
    }

    private void existBook(Book book){
        if(book == null){
            throw new RuntimeException("book not found");
        }
    }

    private void reactionProcess(Integer bookId, boolean status){
        Personal personal = personalRepository.findByEmail(SecurityUtil.getUsername());
        Book book = bookRepository.findById(bookId).orElse(null);
        if(status){
            reactionService.like(book.getId(), personal.getId());
        } else {
            reactionService.dislike(book.getId(), personal.getId());
        }
    }

    private void hasReactionToBook(Book book, Integer personalId){
        existBook(book);
        if(book.getPersonal().getId().equals(personalId)){
            throw new RuntimeException("you do not have a reaction to this post");
        }
    }
}
