import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Check if we are running in the browser (SSR safety)
  const isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
  const router = inject(Router);

  const noAuth = req.headers.get('No-Auth') === 'True';
  const cleanHeaders = req.headers.delete('No-Auth');

  // Start with the clean request (No-Auth header removed)
  let finalReq = req.clone({ headers: cleanHeaders });

  // 2. ONLY access localStorage and attach token if No-Auth is NOT true AND we are in the browser
  if (!noAuth && isBrowser) {
    const authToken = localStorage.getItem('jwtToken'); // Kept your exact key 'jwtToken'

    if (authToken) {
      finalReq = finalReq.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`,
        },
      });
    }
  }

  // 3. Send the request and handle 401 errors globally
  return next(finalReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // ✅ NEW: Check if the backend returned a 401 Unauthorized
      if (error.status === 401) {
        console.warn('🔒 Session expired or invalid token. Logging out...');

        if (isBrowser) {
          // Clear the token using your exact key
          localStorage.removeItem('jwtToken');
        }

        // Redirect to login page
        router.navigate(['/login']);
      }

      // Pass the error to the component so it can show a UI message if needed
      return throwError(() => error);
    })
  );
};
