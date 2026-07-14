package com.online_book_store.book_service.dto.respond;

import com.online_book_store.book_service.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookPaginatedResponseDto {
    List<Book> books;
    private Long  totalBookCount;
}
