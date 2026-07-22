import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject, Observable, map, take } from 'rxjs';
import { OrderService } from './order-service';
import { ToastrService } from 'ngx-toastr';

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
  private cartItemsSubject = new BehaviorSubject<CartItem[]>(this.loadCartFromStorage());

  public cartItems$ = this.cartItemsSubject.asObservable();

  public itemCount$: Observable<number> = this.cartItems$.pipe(
    map((items) => items.reduce((total, item) => total + item.quantity, 0))
  );

  public total$: Observable<number> = this.cartItems$.pipe(
    map((items) => items.reduce((total, item) => total + item.price * item.quantity, 0))
  );

  public paymentIntentReady$ = new BehaviorSubject<string | null>(null);

  constructor(
    private orderService: OrderService,
    private toastr: ToastrService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    console.log('🔧 CartService initialized. Cart value:', this.cartItemsSubject.value);
  }

  private loadCartFromStorage(): CartItem[] {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        const stored = localStorage.getItem('bookStoreCart');
        console.log('📖 Loading from localStorage:', stored);
        if (stored) {
          const parsed = JSON.parse(stored);
          console.log('✅ Successfully parsed cart:', parsed);
          return parsed;
        }
      }
    } catch (e) {
      console.error('❌ Error loading cart from localStorage:', e);
    }
    return [];
  }

  private saveCartToStorage(items: CartItem[]) {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.setItem('bookStoreCart', JSON.stringify(items));
        console.log('💾 Cart saved to localStorage');
      }
    } catch (e) {
      console.error('❌ Error saving cart to localStorage:', e);
    }
  }

  initiateCheckout() {
    const currentItems = this.cartItemsSubject.value;
    if (currentItems.length === 0) {
      this.showToast('error', 'Cart is empty!', 'Error');
      return;
    }

    this.total$.pipe(take(1)).subscribe((total: number) => {
      const amountInCents = Math.round(total * 100);

      // ✅ Send cart items to backend along with amount
      this.orderService.createCheckoutSession(amountInCents, currentItems).subscribe({
        next: (response: any) => {
          console.log('Redirecting to Stripe:', response.url);

          // ✅ Clear cart NOW - backend has a copy of the items

          // Redirect to Stripe
          window.location.href = response.url;
        },
        error: (error) => {
          console.error('Error creating checkout session:', error);
          this.showToast('error', 'Failed to initialize payment.', 'Error');
        },
      });
    });
  }

  addToCart(book: any) {
    const currentItems = this.cartItemsSubject.value;
    const existingItem = currentItems.find((item) => item.id === book.id);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      currentItems.push({ id: book.id, title: book.title, price: book.price, quantity: 1 });
    }

    const updatedCart = [...currentItems];
    this.cartItemsSubject.next(updatedCart);
    this.saveCartToStorage(updatedCart);
  }

  removeItem(itemId: number) {
    const currentItems = this.cartItemsSubject.value;
    const updatedItems = currentItems.filter((item) => item.id !== itemId);
    this.cartItemsSubject.next(updatedItems);
    this.saveCartToStorage(updatedItems);
  }

  decreaseQuantity(itemId: number) {
    const currentItems = this.cartItemsSubject.value;
    const item = currentItems.find((item) => item.id === itemId);
    if (item) {
      if (item.quantity > 1) {
        item.quantity -= 1;
        const updatedCart = [...currentItems];
        this.cartItemsSubject.next(updatedCart);
        this.saveCartToStorage(updatedCart);
      } else {
        this.removeItem(itemId);
      }
    }
  }

  increaseQuantity(itemId: number) {
    const currentItems = this.cartItemsSubject.value;
    const item = currentItems.find((item) => item.id === itemId);
    if (item) {
      item.quantity += 1;
      const updatedCart = [...currentItems];
      this.cartItemsSubject.next(updatedCart);
      this.saveCartToStorage(updatedCart);
    }
  }

  clearCart() {
    this.cartItemsSubject.next([]);
    this.saveCartToStorage([]);
    this.paymentIntentReady$.next(null);
  }

  private showToast(type: 'success' | 'error', message: string, title: string) {
    if (isPlatformBrowser(this.platformId)) {
      if (type === 'success') {
        this.toastr.success(message, title);
      } else {
        this.toastr.error(message, title);
      }
    }
  }
}
