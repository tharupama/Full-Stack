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
}
