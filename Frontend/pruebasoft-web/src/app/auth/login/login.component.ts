import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment.development';

const duration = environment.toast_duration;
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles:  [],
})
export class LoginComponent implements OnInit{

  hide: boolean = true;
  readonly MENSAJE_SIN_CONEXION =
            'No se ha podido establecer una conexión con el servidor, por favor intente mas tarde.';
  
  readonly TITULO_SIN_CONEXION = '¡Sin conexión!';

  loginForm: FormGroup = this.fb.group({
    username: [localStorage.getItem('email') || '', [Validators.required]],
    password: ['', [Validators.required]],
    remember: [false]

  });


  constructor(private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private toast: ToastrService) { }


  ngOnInit(): void {
    if(this.authService.isAuthenticathed()){ 

        this.toast.info(`${this.authService._usuario.username}, ya estas autenticado!`, 'Login',{ timeOut: duration });
        this.router.navigateByUrl('/');
    }
  }


  login() {
    //validar campos
    if(this.loginForm.invalid){
        return Object.values(this.loginForm.controls).forEach(control=> {
        control.markAllAsTouched();
      });
    }
    //realizar login
    this.authService.login(this.loginForm.value)
      .subscribe({
        next: (resp) => {
          if (this.loginForm.get('remember').value) {
            localStorage.setItem('email', this.loginForm.get('username').value);
          } else {
            localStorage.removeItem('email');
          }

          this.router.navigateByUrl('/');
          this.toast.success(`${this.authService._usuario.username}, has iniciado sesión con éxito`,
            'Login!!!', { timeOut: duration });
            
        },
        error: (err) => {console.log(err);
          if (err.error.error === 'Bad credentials' || err.error.error==='User is disabled') {
            this.toast.error(err.error.message, 'Error', { timeOut: duration });
          }else if(err.status ===0){
              this.toast.error(this.MENSAJE_SIN_CONEXION, this.TITULO_SIN_CONEXION, { timeOut: duration });
          }else{
            this.toast.warning(err.error.error, 'Alerta!!!', { timeOut: duration });
          }
        },
      })

  }


  isValido(param: any) {
    return this.loginForm.get(param).invalid && this.loginForm.get(param).touched;
  }

  ocultarPass() {
    this.hide = !this.hide;
  }
}
