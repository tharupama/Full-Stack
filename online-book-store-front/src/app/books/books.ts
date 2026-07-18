import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { BookService } from '../service/book-service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs/internal/Subscription';
import { CartService } from '../service/cart-service';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { UiService } from '../service/ui-service';

@Component({
  selector: 'app-books',
  imports: [CommonModule, MatIconModule, FormsModule],
  templateUrl: './books.html',
  styleUrl: './books.css',
})
export class Books implements OnInit, OnDestroy {
  searchTerm: string = '';
  cartItemCount = 0;
  private subscription!: Subscription;
  books: any[] = [];
  totalBooks: number = 0;
  currentPage: number = 0;
  pageSize: number = 4;
  constructor(
    private cartService: CartService,
    private bookService: BookService,
    private cdr: ChangeDetectorRef,
    private toastr: ToastrService,
    private uiService: UiService
  ) {}
  ngOnInit(): void {
    this.loadBooks(this.currentPage, this.pageSize);
    this.subscription = this.cartService.itemCount$.subscribe((count) => {
      this.cartItemCount = count;
    });
  }
  ngOnDestroy() {
    // Prevent memory leaks
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
  openCartDrawer() {
    console.log('Opening cart drawer...');
    this.uiService.openCart();
  }
  searchBooks() {
    if (this.searchTerm.trim() === '') {
      this.loadBooks(this.currentPage, this.pageSize);
    } else {
      this.bookService.searchBooks(this.searchTerm, this.currentPage, this.pageSize).subscribe(
        (res) => {
          const payload = res?.data ?? res;
          const books = payload?.books ?? [];
          this.books = books.map((b: any) => ({
            id: b.id,
            title: b.title,
            image: b.imgUrl,
            category: b.category,
            author: b.author,
            description: b.description,
            price: b.price,
            stock: b.stock,
            isAvailable: b.isAvailable,
          }));
          this.totalBooks = payload?.totalBookCount ?? 0;
          this.currentPage = this.searchTerm ? 0 : this.currentPage;
          this.cdr.markForCheck();
        },
        (err) => {
          console.error('Error searching books:', err);
          this.toastr.error('Error searching books.');
        }
      );
    }
  }
  loadBooks(page: number, size: number) {
    this.bookService.getBooksByPage(page, size).subscribe(
      (res) => {
        const payload = res?.data ?? res;
        const books = payload?.books ?? [];
        this.books = books.map((b: any) => ({
          id: b.id,
          title: b.title,
          image: b.imgUrl,
          category: b.category,
          author: b.author,
          description: b.description,
          price: b.price,
          stock: b.stock,
          isAvailable: b.isAvailable,
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
  addToCart(book: any) {
    if (book.stock > 0) {
      this.cartService.addToCart(book);
      this.toastr.success(`${book.title} added to cart!`);
    } else {
      this.toastr.error(`${book.title} is out of stock!`);
    }
  }
}
