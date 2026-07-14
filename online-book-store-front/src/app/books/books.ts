import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-books',
  imports: [CommonModule],
  templateUrl: './books.html',
  styleUrl: './books.css',
})
export class Books {
  books = [
    {
      title: 'Science Fiction',
      price: '$19.99',
      image:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8bPquh2GukBG4XuVuzpG3t4zM_wpEAvZb5-7hOXPwyA&s=10',
      description:
        'A fast-moving adventure through distant worlds, perfect for readers who enjoy bold ideas and big stakes.',
    },
    {
      title: 'Mystery Night',
      price: '$24.99',
      image:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRG5VjrVH5A8QOPqAdhWw-lWjx_Wc0Hqya5RjzhQxJYvg&s=10',
      description:
        'A tense, page-turning mystery with hidden clues, surprising twists, and a satisfying reveal at the end.',
    },
    {
      title: 'Romance Reads',
      price: '$29.99',
      image:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYPNqHFJer1MbjOZkii4TSTWJieU6Jq-l8g3X6e6zSbg&s=10',
      description:
        'A warm, emotional story about connection, second chances, and the kind of moments readers remember.',
    },
    {
      title: 'Modern Classics',
      price: '$34.99',
      image:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQF85uqjZScFqxOGDockKvF69qzCkDcHADYbhYkil-6ug&s=10',
      description:
        'A curated pick for readers who want elegant writing, memorable characters, and lasting impact.',
    },
  ];
}
