import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { BookDto } from '../../dto/BookDto';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { BookService } from '../service/book-service';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import { BookUpdateModal } from '../book-update-modal/book-update-modal';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { BookUpdateDto } from '../../dto/BookUpdateDto';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbar } from '@angular/material/toolbar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { PeriodicElement } from '../../dto/PeriodicElement';
import { OrderService } from '../service/order-service';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-admin',
  imports: [
    FormsModule,
    CommonModule,
    BookUpdateModal,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
    CurrencyPipe,
    MatToolbar,
    MatTabsModule,
    MatTableModule,
  ],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin implements OnInit {
  Notification_status: string = 'NOT_PERFORMED';
  ELEMENT_DATA: PeriodicElement[] = [];
  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol', 'update', 'delete'];
  dataSource = new MatTableDataSource<PeriodicElement>(this.ELEMENT_DATA);

  searchTerm: string = '';
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
    private orderService: OrderService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog
  ) {}
  ngOnInit(): void {
    this.loadBooks(this.currentPage, this.pageSize);
    this.getNotifications();
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

  getNotifications() {
    console.log('get Notification admin back load');
    this.orderService.getNotifications().subscribe(
      (res) => {
        const notifications = res;
        this.ELEMENT_DATA = notifications.map((n: any) => ({
          position: n.notificationId,
          name: n.message,
          weight: n.createdAt,
          symbol: n.status,
        }));
        this.dataSource.data = this.ELEMENT_DATA;
        console.log(this.ELEMENT_DATA);
      },
      (err) => {
        console.error('error loading notific', err);
        this.toastr.error('Error loading notifications');
      }
    );
  }

  updateNotifications(element: any) {
    console.log('update notification triggered');
    this.orderService.updateNotifications(element.position, element.symbol).subscribe(
      (res) => {
        const payload = res?.data ?? res;
        this.toastr.success(payload);
      },
      (err) => {
        this.toastr.error('update failed');
      }
    );
  }

  deleteNotification(element: any) {
    console.log('delete triggered from admin ts');
    this.orderService.deleteNotification(element.position).subscribe(
      (res) => {
        const payload = res?.data ?? res;
        this.toastr.success(payload);
        this.getNotifications();
      },
      (err) => {
        this.toastr.error('Delete dailed');
      }
    );
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
  updateBook(book: any) {
    const modalData: BookUpdateDto = {
      id: book.id,
      title: book.title,
      imgUrl: book.image, // Backend expects 'imgUrl'
      category: book.category,
      author: book.author,
      description: book.description,
      price: parseFloat(book.price.toString().replace('$', '')),
      stock: book.stock,
      isAvailable: book.isAvailable,
    };

    const dialogRef = this.dialog.open(BookUpdateModal, {
      width: '500px',
      data: modalData,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        console.log('Payload being sent to Spring Boot:', result);

        this.bookService.updateBook(result).subscribe(
          (response) => {
            console.log('Book updated:', response);
            this.toastr.success('Book updated successfully!');
            this.loadBooks(this.currentPage, this.pageSize);
          },
          (error) => {
            console.error('Error updating book:', error);
            this.toastr.error('Error updating book.');
          }
        );
      }
    });
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
  deleteBook(bookId: number) {
    Swal.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
    }).then((result) => {
      if (result.isConfirmed) {
        this.bookService.deleteBook(bookId).subscribe(
          (response) => {
            console.log('Book deleted:', response);
            this.toastr.success('Book deleted successfully!');
            this.loadBooks(this.currentPage, this.pageSize); // Reload books after deletion
          },
          (error) => {
            console.error('Error deleting book:', error);
            this.toastr.error('Error deleting book.');
          }
        );
      }
    });
  }
}
