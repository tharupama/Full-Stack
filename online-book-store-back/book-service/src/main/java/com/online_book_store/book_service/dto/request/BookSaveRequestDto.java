package com.online_book_store.book_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookSaveRequestDto {
    private String title;
    private String imgUrl;
    private String category;
    private String author;
    private String description;
    private double price;
    private int stock;
    private Boolean isAvailable;
}
