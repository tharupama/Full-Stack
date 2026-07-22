import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  BASE_URL = 'http://localhost:9191';
  requestHeaderWithAuth = new HttpHeaders({ 'No-Auth': 'False' });

  constructor(private httpClient: HttpClient) {}

  // ✅ UPDATED: Now sends cart items and user email
  public createCheckoutSession(amount: number, cartItems: any[]): Observable<any> {
    console.log('Creating checkout session for amount (in cents):', amount);
    const url = `${this.BASE_URL}/api/v1/order-controller/create-checkout-session`;
    return this.httpClient.post<any>(
      url,
      { amount, cartItems },
      { headers: this.requestHeaderWithAuth }
    );
  }

  // ❌ REMOVED: placeOrder() is no longer needed
  // Orders are now created securely via Stripe webhooks in the backend

  public getNotifications(): Observable<any> {
    const url = `${this.BASE_URL}/api/v1/notification-controller/getNotification`;
    return this.httpClient.get<any>(url, { headers: this.requestHeaderWithAuth });
  }

  public updateNotifications(id: Number, status: string): Observable<any> {
    const url = `${this.BASE_URL}/api/v1/notification-controller/updateNotification?notificationId=${id}&status=${status}`;
    return this.httpClient.put<any>(url, null, { headers: this.requestHeaderWithAuth });
  }

  public deleteNotification(id: Number): Observable<any> {
    const url = `${this.BASE_URL}/api/v1/notification-controller/deleteNotification/${id}`;
    return this.httpClient.delete(url, { headers: this.requestHeaderWithAuth });
  }
}
