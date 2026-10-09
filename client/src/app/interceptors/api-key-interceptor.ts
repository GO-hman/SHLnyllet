import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class ApiKeyInterceptor implements HttpInterceptor {
  private apiKey = environment.apiKey;

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const urlPattern = environment.apiBasePath;
    
    if (req.url.includes(urlPattern)) {
      const secureReq = req.clone({
        headers: req.headers.set('X-API-KEY', this.apiKey),
      });
      return next.handle(secureReq);
    }

    return next.handle(req);
  }
}
