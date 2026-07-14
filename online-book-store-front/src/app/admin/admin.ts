import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { BookDto } from '../../dto/BookDto';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BookService } from '../service/book-service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin',
  imports: [FormsModule, CommonModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin implements OnInit {
  bookDto: BookDto = {
    title: '',
    imgUrl: '',
    category: '',
    author: '',
    description: '',
    price: 0,
    stock: 0,
    isAvailable: true,
  };
  books: any[] = [];
  totalBooks: number = 0;
  currentPage: number = 0;
  pageSize: number = 4;
  constructor(
    private bookService: BookService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}
  ngOnInit(): void {
    this.loadBooks(this.currentPage, this.pageSize);
  }
  addBook() {
    this.bookService.saveBook(this.bookDto).subscribe(
      (response) => {
        console.log('Book added:', response);
        this.toastr.success('Book added successfully!');
        this.bookDto = {
          title: '',
          imgUrl: '',
          category: 'science-fiction',
          author: '',
          description: '',
          price: 0,
          stock: 0,
          isAvailable: true,
        }; // Clear the form after successful submission
        this.cdr.markForCheck();
      },
      (error) => {
        console.error('Error adding book:', error);
        this.toastr.error('Error adding book.');
      }
    );
  }

  loadBooks(page: number, size: number) {
    this.bookService.getBooksByPage(page, size).subscribe(
      (res) => {
        const payload = res?.data ?? res;
        const books = payload?.books ?? [];
        this.books = books.map((b: any) => ({
          title: b.title,
          image: b.imgUrl,
          price: '$' + (typeof b.price === 'number' ? b.price.toFixed(2) : b.price),
          description: b.description,
        }));
        this.totalBooks = payload?.totalBookCount ?? 0;
        this.currentPage = page;
        this.pageSize = size;
        this.cdr.markForCheck();
      },
      (err) => {
        console.error('Error loading books:', err);
        this.toastr.error('Error loading books.');
      }
    );
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.loadBooks(this.currentPage - 1, this.pageSize);
    }
  }

  nextPage() {
    const maxPage = Math.ceil(this.totalBooks / this.pageSize) - 1;
    if (this.currentPage < maxPage) {
      this.loadBooks(this.currentPage + 1, this.pageSize);
    }
  }
}
