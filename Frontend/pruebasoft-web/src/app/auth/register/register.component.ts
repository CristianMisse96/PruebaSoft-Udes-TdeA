import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';
import { environment } from 'src/environments/environment.development';

const duration = environment.toast_duration;
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styles: [
  ]
})
export class RegisterComponent implements OnInit{
  
  constructor(private authService: AuthService,
              private router: Router,
              private toast: ToastrService) {}
  ngOnInit(): void {
    if(this.authService.isAuthenticathed()){ 

      this.toast.info(`${this.authService._usuario.username}, ya estas autenticado!`, 'Registro',{ timeOut: duration });
      this.router.navigateByUrl('/');
    }
  }

}
