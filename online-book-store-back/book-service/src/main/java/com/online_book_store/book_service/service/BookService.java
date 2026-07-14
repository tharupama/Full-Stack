package com.online_book_store.book_service.service;

import com.online_book_store.book_service.dto.request.BookSaveRequestDto;
import com.online_book_store.book_service.dto.respond.BookPaginatedResponseDto;
import com.online_book_store.book_service.entity.Book;

public interface BookService{
    String bookSave(BookSaveRequestDto book);

    String bookUpdate(Book book);

    String deleteBook(Long id);

    BookPaginatedResponseDto getBooksByPage(int page, int size);
}
