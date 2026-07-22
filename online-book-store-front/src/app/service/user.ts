import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SignUp } from '../../dto/SignUp';
import { UserAuth } from './user-auth';

@Injectable({
  providedIn: 'root',
})
export class User {
  BASE_URL = 'http://localhost:9191';
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });
  requestHeaderWithAuth = new HttpHeaders({ 'No-Auth': 'False' });
  constructor(private httpClient: HttpClient, private userAuthService: UserAuth) {}

  public login(loginData: any) {
    return this.httpClient.post(this.BASE_URL + '/login', loginData, {
      headers: this.requestHeader,
    });
  }

  public registerNewUser(signup: SignUp): Observable<SignUp> {
    return this.httpClient.post<SignUp>(`${this.BASE_URL}/signup`, signup, {
      headers: this.requestHeader,
    });
  }

  public roleEqual(allowRoles: Array<String>): boolean {
    const userRoles: any = this.userAuthService.getRoles();

    // If user has no roles, deny access
    if (!userRoles || userRoles.length === 0) {
      return false;
    }

    // Check if any user role matches any allowed role
    //for (const userRole of userRoles) {
    if (allowRoles.includes(userRoles)) {
      return true;
    }
    // }
    return false;
  }
  public getUserProfile(): Observable<any> {
    const url = `${this.BASE_URL}/profile`;
    return this.httpClient.get<any>(url, { headers: this.requestHeaderWithAuth });
  }
  public updateUserProfile(profileData: any): Observable<any> {
    console.log('Updating user profile:', profileData);
    const url = `${this.BASE_URL}/save-more-details`;
    return this.httpClient.put<any>(url, profileData, { headers: this.requestHeaderWithAuth });
  }
}
