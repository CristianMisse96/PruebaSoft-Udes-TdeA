import { inject } from "@angular/core";
import { CanMatchFn, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { AuthService } from "../services/auth.service";

export const canMatch: CanMatchFn = () => {

    const authService = inject(AuthService);
    const router = inject(Router);
    const toast= inject(ToastrService);
  
    if(authService.isAuthenticathed()){
      if (authService.isTokenExpirado()) {
        authService.logout();
        toast.info('Por favor, inicia sesión nuevamente para continuar.','Su sesión ha expirado', {timeOut:2500})
        router.navigate(['/login']);
        return false;
      }
      return true;
    }
    
    router.navigateByUrl('/login');
    return false;
  }