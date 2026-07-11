import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID } from '@angular/core';
import { UserAuth } from '../service/user-auth';
import { User } from '../service/user';

export const authGuard: CanActivateFn = (route) => {
  const authService = inject(UserAuth);
  const userService = inject(User);
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  // During SSR, localStorage is unavailable. Let the client perform auth checks.
  if (!isPlatformBrowser(platformId)) {
    return true;
  }

  if (!authService.isLoggedIn()) {
    //console.log('not logged');
    return router.parseUrl('/login');
  }

  const roles = route.data['roles'] as string[] | undefined;
  if (!roles || roles.length === 0) {
    return true;
  }

  return userService.roleEqual(roles) ? true : router.parseUrl('/forbidden');
};
