import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { environment } from 'src/environments/environment.development';

const duration= environment.toast_duration;
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private readonly ACCESO_NO_AUTORIZADO='Acceso no autorizado. Inicia sesión o contacta al administrador.';
  private readonly ACCESO_DENEGADO='Acceso Denegado';
  private readonly MSG_ACCESO_DENEGADO='no tienes acceso a este recurso';

  constructor(private toast: ToastrService, 
              private router: Router,
              private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(e=> {
        if(e.status==401){
          if(this.authService.isAuthenticathed()){
            this.authService.logout();
            this.toast.error(this.ACCESO_NO_AUTORIZADO, 'Error!!',{timeOut:duration});
          }
          
          this.router.navigate(['/login']);
        }

        if(e.status==403){
          this.toast.warning(this.MSG_ACCESO_DENEGADO,this.ACCESO_DENEGADO,{timeOut:duration});
          this.router.navigate(['/']);
        }

        return throwError(()=>e);
      }),
    );
  }
}
