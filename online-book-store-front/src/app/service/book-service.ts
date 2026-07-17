import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { BookDto } from '../../dto/BookDto';
import { Observable } from 'rxjs/internal/Observable';
import { BookUpdateDto } from '../../dto/BookUpdateDto';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  BASE_URL = 'http://localhost:9191';
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });
  requestHeaderWithAuth = new HttpHeaders({ 'No-Auth': 'False' });
  constructor(private httpClient: HttpClient) {}

  public saveBook(bookData: BookDto): Observable<BookDto> {
    console.log('Saving book:', bookData);
    return this.httpClient.post<BookDto>(this.BASE_URL + '/api/v1/book-controller/save', bookData, {
      headers: this.requestHeaderWithAuth,
    });
  }

  public getBooksByPage(page: number, size: number): Observable<any> {
    const url = `${this.BASE_URL}/api/v1/book-controller/get-by-page?page=${page}&size=${size}`;
    return this.httpClient.get<any>(url, { headers: this.requestHeaderWithAuth });
  }
  public deleteBook(bookId: number): Observable<any> {
    const url = `${this.BASE_URL}/api/v1/book-controller/delete/${bookId}`;
    return this.httpClient.delete<any>(url, { headers: this.requestHeaderWithAuth });
  }
  public updateBook(bookData: BookUpdateDto): Observable<BookUpdateDto> {
    console.log('Updating book:', bookData);
    return this.httpClient.put<BookUpdateDto>(
      this.BASE_URL + '/api/v1/book-controller/update',
      bookData,
      {
        headers: this.requestHeaderWithAuth,
      }
    );
  }
  public searchBooks(searchTerm: string, page: number, size: number): Observable<any> {
    const url = `${
      this.BASE_URL
    }/api/v1/book-controller/get-by-page-and-title?title=${encodeURIComponent(
      searchTerm
    )}&page=${page}&size=${size}`;
    return this.httpClient.get<any>(url, { headers: this.requestHeaderWithAuth });
  }
}
