import { Routes } from '@angular/router';
import { Login } from './login/login';
import { App } from './app';
import { Home } from './home/home';
import { Register } from './register/register';
import { Admin } from './admin/admin';
import { authGuard } from './auth/auth-guaed';
import { Forbidden } from './forbidden/forbidden';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'admin', component: Admin, canActivate: [authGuard], data: { roles: ['Admin'] } },
  { path: 'forbidden', component: Forbidden },
];
