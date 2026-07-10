import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { BidiModule } from '@angular/cdk/bidi';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, BidiModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  invalid: boolean = false;
  login(loginForm: NgForm) {
    if (loginForm.valid) {
      console.log('Form submitted:', loginForm.value);
    } else {
      console.log('Form is invalid');
    }
  }
}
