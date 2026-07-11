import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { BidiModule } from '@angular/cdk/bidi';
import { OnInit } from '@angular/core';
import { UserAuth } from '../service/user-auth';
import { User } from '../service/user';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, BidiModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  constructor(
    private userAuthService: UserAuth,
    private userService: User,
    private router: Router,
    private toastr: ToastrService
  ) {}
  ngOnInit() {}
  /* invalid: boolean = false; */
  login(loginForm: NgForm) {
    if (loginForm.valid) {
      console.log('Form submitted:', loginForm.value);
      this.userService.login(loginForm.value).subscribe(
        (response: any) => {
          console.log('Login successful:', response);
          this.toastr.success('Login successful', 'Success');
          this.userAuthService.setToken(response.token);
          this.userAuthService.setRoles(response.user.role);
          this.userAuthService.setUsername(response.user.username);
          const role = response.user.role;
          if (role === 'Admin') {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['']);
          }
        },
        (error: any) => {
          console.error('Login failed:', error);
          this.toastr.error('Invalid username or password', 'Login Error');
        }
      );
    } else {
      console.log('Form is invalid');
    }
  }
}
