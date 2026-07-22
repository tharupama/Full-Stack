import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../service/cart-service';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-payment-success',
  imports: [RouterLink, MatButtonModule],
  templateUrl: './payment-success.html',
  styleUrl: './payment-success.css',
})
export class PaymentSuccess {
  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit() {
    setTimeout(() => {
      console.log('Payment succeeded! Clearing local cart...');
      this.cartService.clearCart();
    }, 0);
  }
}
