package com.online_book_store.book_service.service.impl;


import com.online_book_store.book_service.Util.mappers.BookMapper;
import com.online_book_store.book_service.dto.request.BookSaveRequestDto;
import com.online_book_store.book_service.dto.respond.BookPaginatedResponseDto;
import com.online_book_store.book_service.entity.Book;
import com.online_book_store.book_service.exception.NotFoundException;
import com.online_book_store.book_service.repository.BookRepository;
import com.online_book_store.book_service.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private BookMapper bookMapper;
    private BookRepository bookRepository;
    @Override
    public String bookSave(BookSaveRequestDto book) {
        bookRepository.save(bookMapper.bookRequestDtoToBookEntity(book));
        return book.getTitle() +" saved successfully";
    }

    @Override
    public String bookUpdate(Book book) {
        if(bookRepository.findById(book.getId()).isPresent()){
            bookRepository.save(book);
            return book.getTitle() +" updated successfully";
        }else{
            throw new NotFoundException("Book not found");
        }

    }

    @Override
    public String deleteBook(Long id) {
        if(bookRepository.findById(id).isPresent()){
            bookRepository.deleteById(id);
            return "Book deleted successfully";
        } else {
            throw new NotFoundException("Book not found");
        }

    }

    @Override
    public BookPaginatedResponseDto getBooksByPage(int page, int size) {
        Page<Book> bookPage = bookRepository.findAll(PageRequest.of(page, size));
        BookPaginatedResponseDto bookPaginatedResponseDto = new BookPaginatedResponseDto(bookMapper.pageBookListToListBook(bookPage), bookRepository.count());
        return bookPaginatedResponseDto;
    }

    @Override
    public BookPaginatedResponseDto getBooksByPageAndTitle(String title,int page, int size) {

        Page<Book> bookPage = bookRepository.findAllByTitleContaining(title,PageRequest.of(page, size));
        BookPaginatedResponseDto bookPaginatedResponseDto = new BookPaginatedResponseDto(bookMapper.pageBookListToListBook(bookPage), bookRepository.count());
        return bookPaginatedResponseDto;
    }
}
