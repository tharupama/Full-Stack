import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface CartItem {
  id: number;
  title: string;
  price: number;
  quantity: number;
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  // BehaviorSubject holds the current state of the cart
  private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);

  // Observable for components to subscribe to
  public cartItems$ = this.cartItemsSubject.asObservable();

  // Observable specifically for the item count (for the sticky button badge)
  public itemCount$: Observable<number> = this.cartItems$.pipe(
    map((items) => items.reduce((total, item) => total + item.quantity, 0))
  );

  constructor() {}

  // Method to add a book to the cart
  addToCart(book: any) {
    const currentItems = this.cartItemsSubject.value;
    const existingItem = currentItems.find((item) => item.id === book.id);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      currentItems.push({ id: book.id, title: book.title, price: book.price, quantity: 1 });
    }

    this.cartItemsSubject.next([...currentItems]);
  }

  // Method to clear the cart
  clearCart() {
    this.cartItemsSubject.next([]);
  }
}
