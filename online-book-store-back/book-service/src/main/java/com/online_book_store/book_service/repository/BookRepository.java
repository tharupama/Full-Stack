package com.online_book_store.book_service.repository;

import com.online_book_store.book_service.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookRepository extends JpaRepository<Book,Long> {
    Page<Book> findAllByTitleContaining(String title, Pageable pageable);
}
