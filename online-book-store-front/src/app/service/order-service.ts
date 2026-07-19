import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  BASE_URL = 'http://localhost:8082';
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });
  requestHeaderWithAuth = new HttpHeaders({ 'No-Auth': 'False' });
  constructor(private httpClient: HttpClient) {}

  public getNotifications(): Observable<any> {
    console.log('get notification function triggered in order service');
    const url = `${this.BASE_URL}/api/v1/notification-controller/getNotification`;
    return this.httpClient.get<any>(url, { headers: this.requestHeader });
  }

  public updateNotifications(id: Number, status: string): Observable<any> {
    console.log('update notific in order service triggered');
    const url = `${this.BASE_URL}/api/v1/notification-controller/updateNotification?notificationId=${id}&status=${status}`;
    return this.httpClient.put<any>(url, null, { headers: this.requestHeaderWithAuth });
  }

  public deleteNotification(id: Number): Observable<any> {
    console.log('order service function triggered');
    const url = `${this.BASE_URL}/api/v1/notification-controller/deleteNotification/${id}`;
    return this.httpClient.delete(url, { headers: this.requestHeaderWithAuth });
  }
}
