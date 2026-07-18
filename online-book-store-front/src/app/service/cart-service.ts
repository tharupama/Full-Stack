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
  // ✅ ADD THIS: Remove a single item completely
  removeItem(itemId: number) {
    const currentItems = this.cartItemsSubject.value;
    const updatedItems = currentItems.filter((item) => item.id !== itemId);
    this.cartItemsSubject.next(updatedItems);
  }

  // ✅ ADD THIS: Decrease quantity by 1 (remove if quantity reaches 0)
  decreaseQuantity(itemId: number) {
    const currentItems = this.cartItemsSubject.value;
    const item = currentItems.find((item) => item.id === itemId);

    if (item) {
      if (item.quantity > 1) {
        item.quantity -= 1;
        this.cartItemsSubject.next([...currentItems]);
      } else {
        // If quantity is 1, remove the item completely
        this.removeItem(itemId);
      }
    }
  }

  // ✅ ADD THIS: Increase quantity by 1
  increaseQuantity(itemId: number) {
    const currentItems = this.cartItemsSubject.value;
    const item = currentItems.find((item) => item.id === itemId);

    if (item) {
      item.quantity += 1;
      this.cartItemsSubject.next([...currentItems]);
    }
  }

  // Method to clear the cart
  clearCart() {
    this.cartItemsSubject.next([]);
  }
  public total$: Observable<number> = this.cartItems$.pipe(
    map((items) => items.reduce((total, item) => total + item.price * item.quantity, 0))
  );
}
