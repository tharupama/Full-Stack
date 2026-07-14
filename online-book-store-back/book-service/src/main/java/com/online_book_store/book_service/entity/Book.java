package com.online_book_store.book_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String imgUrl;
    private String category;
    private String author;
    private String description;
    private double price;
    private int stock;
    private Boolean isAvailable;

}
