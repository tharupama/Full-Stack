import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { SignUp } from '../../dto/SignUp';
import { User } from '../service/user';
import { Router } from '@angular/router';

import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule],
  standalone: true,
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  constructor(private userService: User, private router: Router, private toastr: ToastrService) {}
  signup: SignUp = {
    username: '',
    email: '',
    password: '',
  };
  register() {
    this.userService.registerNewUser(this.signup).subscribe(
      (response) => {
        this.toastr.success('User registered successfully', 'Success');
        this.router.navigate(['/login']);
      },
      (error) => {
        console.error('Error registering user: ', error);
        this.toastr.error('Error registering user', 'Error');
      }
    );
  }
}
