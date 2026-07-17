import { HttpInterceptorFn } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Check if we are running in the browser (not on the Node.js server)
  const isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

  const noAuth = req.headers.get('No-Auth') === 'True';
  const cleanHeaders = req.headers.delete('No-Auth');
  const cleanReq = req.clone({ headers: cleanHeaders });

  if (noAuth) {
    return next(cleanReq);
  }

  // 2. ONLY access localStorage if we are in the browser
  let authToken: string | null = null;
  if (isBrowser) {
    authToken = localStorage.getItem('jwtToken');
  }

  // 3. If we have a token, attach it
  if (authToken) {
    const authReq = cleanReq.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`,
      },
    });
    return next(authReq);
  }

  // 4. Otherwise, send the clean request
  return next(cleanReq);
};
