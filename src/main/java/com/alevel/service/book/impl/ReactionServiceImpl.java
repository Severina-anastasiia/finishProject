package com.alevel.service.book.impl;

import com.alevel.presistence.entity.book.Book;
import com.alevel.presistence.entity.book.Reaction;
import com.alevel.presistence.entity.user.Personal;
import com.alevel.presistence.repository.book.BookRepository;
import com.alevel.presistence.repository.book.ReactionRepository;
import com.alevel.presistence.repository.user.PersonalRepository;
import com.alevel.service.book.ReactionService;
import com.alevel.util.BookUtil;
import com.alevel.util.SecurityUtil;
import com.alevel.web.data.KeyValueData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PersonalRepository personalRepository;
    private final BookRepository bookRepository;

    public ReactionServiceImpl(ReactionRepository reactionRepository, PersonalRepository personalRepository, BookRepository bookRepository) {
        this.reactionRepository = reactionRepository;
        this.personalRepository = personalRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void deleteByBookId(Integer bookId) {
        List<Reaction> reactions = reactionRepository.findAllByBookId(bookId);
        if(CollectionUtils.isNotEmpty(reactions)){
            reactionRepository.deleteAll(reactions);
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void like(Integer bookId, Integer personalId) {
        reactionProcess(bookId, personalId, true);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void dislike(Integer bookId, Integer personalId) {
        reactionProcess(bookId, personalId, false);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public List<Reaction> findAllByBookIdAndLikeTrue(Integer bookId) {
        return reactionRepository.findAllByBookIdAndLikeTrue(bookId);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public List<Reaction> findAllByBookIdAndLikeFalse(Integer bookId) {
        return reactionRepository.findAllByBookIdAndLikeFalse(bookId);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    @Transactional(readOnly = true)
    public Map<String, List<KeyValueData<Date, Long>>> generatedChartByBookReaction() {
        List<Integer> bookIds = generateAllBookIdListByPersonal();
        if(CollectionUtils.isEmpty(bookIds)){
            return Collections.emptyMap();
        }
        Map<String, List<KeyValueData<Date, Long>>> chartDataMap = new HashMap<>();
        chartDataMap.put(BookUtil.BOOK_ALL, reactionRepository.generateAllBookWithoutReaction(bookIds));
        chartDataMap.put(BookUtil.LIKE_ALL, reactionRepository.generateAllBookByReaction(bookIds, true));
        chartDataMap.put(BookUtil.DISLIKE_ALL, reactionRepository.generateAllBookByReaction(bookIds, false));
        return chartDataMap;
    }

    private void reactionProcess(Integer bookId, Integer personalId, boolean status){
        Reaction reaction = reactionRepository.findByBookIdAndPersonalId(bookId, personalId);
        if(reaction == null){
            reaction = new Reaction();
            reaction.setBookId(bookId);
            reaction.setPersonalId(personalId);
        }
        reaction.setLike(status);
        reactionRepository.save(reaction);
    }

    private List<Integer> generateAllBookIdListByPersonal(){
        Personal personal = personalRepository.findByEmail(SecurityUtil.getUsername());
        List<Book> books = bookRepository.findAllByPersonal(personal);
        if(CollectionUtils.isEmpty(books)){
            return Collections.emptyList();
        }
        return books.stream().map(Book::getId).collect(Collectors.toList());
    }
}
