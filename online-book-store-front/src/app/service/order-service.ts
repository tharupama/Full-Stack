import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderTableDto } from '../../dto/OrderTableDto';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  BASE_URL = 'http://localhost:9191';
  requestHeaderWithAuth = new HttpHeaders({ 'No-Auth': 'False' });

  constructor(private httpClient: HttpClient) {}

  public getOrders(pageSize: Number, pageNumber: Number): Observable<any> {
    const url = `${this.BASE_URL}/api/v1/order-controller/get-orders?page=${pageNumber}&size=${pageSize}`;
    return this.httpClient.get<any>(url, { headers: this.requestHeaderWithAuth });
  }

  public createCheckoutSession(amount: number, cartItems: any[]): Observable<any> {
    console.log('Creating checkout session for amount (in cents):', amount);
    const url = `${this.BASE_URL}/api/v1/order-controller/create-checkout-session`;
    return this.httpClient.post<any>(
      url,
      { amount, cartItems },
      { headers: this.requestHeaderWithAuth }
    );
  }

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
  public getOrderAndCustomerDetails(e: OrderTableDto): Observable<any> {
    console.log('order service getOrderAndCustomerDetails triggered');
    const url = `${this.BASE_URL}/api/v1/order-controller/get-order-customer-details?orderId=${e.orderId}&email=${e.customerId}`;
    return this.httpClient.get<any>(url, { headers: this.requestHeaderWithAuth });
  }
  public updateOrderStatus(orderId: Number, newStatus: String): Observable<any> {
    console.log('order service updateOrderStatus triggered');
    const url = `${this.BASE_URL}/api/v1/order-controller/updateStatus?orderId=${orderId}&status=${newStatus}`;
    return this.httpClient.put<any>(url, null, { headers: this.requestHeaderWithAuth });
  }
}
