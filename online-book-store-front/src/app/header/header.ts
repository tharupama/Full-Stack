import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';
import { UserAuth } from '../service/user-auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [MatToolbarModule, RouterLink, CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  constructor(private userAuth: UserAuth) {}
  public isLoggedIn() {
    return this.userAuth.isLoggedIn();
  }
  public getRole() {
    return this.userAuth.getRoles();
  }
  public clear() {
    this.userAuth.clear();
  }
  public getUsername(): string {
    return this.userAuth.getUsername();
  }
}
