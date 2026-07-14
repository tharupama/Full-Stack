package com.online_book_store.book_service.Util.mappers;

import com.online_book_store.book_service.dto.request.BookSaveRequestDto;
import com.online_book_store.book_service.entity.Book;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(componentModel = "spring")
public interface BookMapper {
    Book bookRequestDtoToBookEntity(BookSaveRequestDto bookRequestDto);
    List<Book> pageBookListToListBook(Page<Book> bookPage);

}
