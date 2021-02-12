package com.alevel.presistence.repository.book;

import com.alevel.presistence.entity.book.Chapter;
import com.alevel.presistence.repository.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends AbstractRepository<Chapter> {

    List<Chapter> findAllByBookId(Integer id);
    Page<Chapter> findAllByBookId(Integer id, Pageable pageable);
    Chapter findByBookId(Integer bookId);
}
