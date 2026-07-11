import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.headers.get('No-Auth') == 'True') {
    return next(req);
  } else {
    const authToken = localStorage.getItem('jwtToken');
    const authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${authToken}` },
    });
    return next(authReq);
  }
};
