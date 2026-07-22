import { Component, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Header } from './header/header';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { Subscription } from 'rxjs';
import { UiService } from './service/ui-service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { CartItem, CartService } from './service/cart-service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    Header,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    RouterLink,
    CommonModule,
    MatButtonModule,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit, OnDestroy {
  @ViewChild('drawer') rightDrawer!: MatSidenav;

  private uiSubscription!: Subscription;
  protected readonly title = signal('online-book-store-front');

  cartItems$!: Observable<CartItem[]>;
  itemCount$!: Observable<number>;

  constructor(
    private uiService: UiService,
    public cartService: CartService // No Stripe services needed here anymore!
  ) {
    this.cartItems$ = this.cartService.cartItems$;
    this.itemCount$ = this.cartService.itemCount$;
  }

  ngOnInit() {
    this.uiSubscription = this.uiService.toggleCart$.subscribe(() => {
      this.rightDrawer?.toggle();
    });
  }

  ngOnDestroy() {
    this.uiSubscription.unsubscribe();
  }

  clearCart() {
    this.cartService.clearCart();
  }

  // Simply triggers the checkout flow in the CartService
  initiatePayment() {
    this.cartService.initiateCheckout();
  }
}
