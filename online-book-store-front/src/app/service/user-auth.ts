import { Injectable } from '@angular/core';
import { Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class UserAuth {
  constructor(@Inject(PLATFORM_ID) private platformId: object) {}
  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  public setRoles(role: String) {
    if (!this.isBrowser()) {
      return;
    }
    localStorage.setItem('role', JSON.stringify(role));
  }

  public getRoles(): String {
    if (!this.isBrowser()) {
      return '';
    }

    const role = localStorage.getItem('role');
    return role ? JSON.parse(role) : '';
  }

  public setToken(jwtToken: string) {
    if (!this.isBrowser()) {
      return;
    }
    localStorage.setItem('jwtToken', jwtToken);
  }

  public setUsername(username: string) {
    if (!this.isBrowser()) {
      return;
    }
    localStorage.setItem('username', username);
  }

  public getToken(): string | null {
    if (!this.isBrowser()) {
      return null;
    }
    return localStorage.getItem('jwtToken');
  }

  public clear() {
    if (!this.isBrowser()) {
      return;
    }
    localStorage.clear();
  }

  public isLoggedIn() {
    const token = this.getToken();
    return token != null;
  }
  public getUsername(): string {
    if (!this.isBrowser()) {
      return '';
    }
    const name: string = localStorage.getItem('username') || '';
    return name;
  }
}
