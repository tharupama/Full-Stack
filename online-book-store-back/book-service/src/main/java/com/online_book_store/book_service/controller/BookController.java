package com.online_book_store.book_service.controller;

import com.online_book_store.book_service.Util.StandardResponse;
import com.online_book_store.book_service.dto.request.BookSaveRequestDto;
import com.online_book_store.book_service.dto.respond.BookPaginatedResponseDto;
import com.online_book_store.book_service.entity.Book;
import com.online_book_store.book_service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@RequestMapping("api/v1/book-controller")
@SecurityRequirement(name = "Bearer Authentication")//for swagger

public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping("/save")
    public ResponseEntity<StandardResponse> save(@RequestBody BookSaveRequestDto book) {
        String returnMsg = bookService.bookSave(book);
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "success",returnMsg), HttpStatus.CREATED);
    }


    @PutMapping("/update")
    public ResponseEntity<StandardResponse> update(@RequestBody Book book) {
        String returnMsg = bookService.bookUpdate(book);
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "success",returnMsg), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> delete(@PathVariable Long id) {
        String msg = bookService.deleteBook(id);
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "success", "Book deleted successfully"), HttpStatus.OK);
    }

    @GetMapping(value = "/get-by-page",params = {"page","size"})
    public ResponseEntity<StandardResponse> getByPage(@RequestParam int page, @RequestParam int size) {
        BookPaginatedResponseDto bookPaginatedResponseDto = bookService.getBooksByPage(page, size);
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "success", bookPaginatedResponseDto), HttpStatus.OK);
    }
    @GetMapping(value = "/get-by-page-and-title",params = {"title","page","size"})
    public ResponseEntity<StandardResponse> getByPage(@RequestParam String title,@RequestParam int page, @RequestParam int size) {
        BookPaginatedResponseDto bookPaginatedResponseDto = bookService.getBooksByPageAndTitle(title,page,size);
        return new ResponseEntity<StandardResponse>(new StandardResponse(200, "success", bookPaginatedResponseDto), HttpStatus.OK);
    }
    @PutMapping("/update-quantity")
    public boolean updateBookQuantity(@RequestParam Long bookId, @RequestParam int quantity) {
        boolean result = bookService.updateBookQuantity(bookId, quantity);
        return result;
    }
}
