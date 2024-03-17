import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { RolEnum } from '../models/enums/RolEnum';
import { ToastrService } from 'ngx-toastr';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const toast= inject(ToastrService);
 
  if(!authService.isAuthenticathed()){
    router.navigate(['/login']);
    return false;
  }

  let role= route.data['role'] as RolEnum;
  console.log(role)
  if (authService.hasRole(role)) {
    return true;
  }


  toast.warning('no tienes acceso a este recurso','Acceso Denegado',{timeOut:2000});
      router.navigate(['/']);
  return false;
};
