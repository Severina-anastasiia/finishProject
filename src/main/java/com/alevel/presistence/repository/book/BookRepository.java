package com.alevel.presistence.repository.book;

import com.alevel.presistence.entity.book.Book;
import com.alevel.presistence.entity.user.Personal;
import com.alevel.presistence.repository.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends AbstractRepository<Book> {

    List<Book> findAllByPersonal(Personal personal);
    Page<Book> findAllByPersonal(Personal personal, Pageable pageable);
    Page<Book> findAllByPersonalIsNot(Personal personal, Pageable pageable);
    Book findByPersonal(Personal personal);
}
