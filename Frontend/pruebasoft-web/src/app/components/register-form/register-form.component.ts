import { Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { FormBuilder, FormControlOptions, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RolEnum } from 'src/app/models/enums/RolEnum';
import { Usuario } from 'src/app/models/usuario.model';
import { AuthService } from 'src/app/services/auth.service';
import { ValidadoresService } from 'src/app/services/validadores.service';
import { environment } from 'src/environments/environment.development';

const duration= environment.toast_duration;

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styles: [`
    .my-custom-invalid {
      border-color: #dc3545; /* Color rojo del borde */
      border-width: 1px; /* Grosor del borde */
      border-style: solid; /* Estilo del borde */
      /* Otros estilos que desees aplicar */
    }
  `
  ]
})
export class RegisterFormComponent implements OnInit{

  hide: boolean = true;
  hide2: boolean = true;
  registerForm: FormGroup;
  rolAdmin: RolEnum= RolEnum.ROLE_ADMIN;
  @Output() salida : EventEmitter<any>= new EventEmitter<any>();
  @Input() modoEditar: boolean;
  @Input() usuario: Usuario | undefined; // Nuevo Input para recibir el usuario

  constructor(private fb: FormBuilder,
              private router: Router,
              public authService: AuthService,
              private validadores: ValidadoresService,
              private toast: ToastrService) {

   
  }
  ngOnInit(): void {
    this.crearFormulario();
    this.cargarDataAlFormulario();
  }

  cargarDataAlFormulario() {
    if(this.modoEditar && this.usuario){
      this.registerForm.patchValue({
        name: this.usuario.nombre,
        apellido: this.usuario.apellido,
        email: this.usuario.email,
        password: this.usuario.password,
        student: this.usuario.roles.includes(RolEnum.ROLE_STUDENT),
        teacher: this.usuario.roles.includes(RolEnum.ROLE_TEACHER),
        admin: this.usuario.roles.includes(RolEnum.ROLE_ADMIN),
      });
    }
  }
 
  crearFormulario() {
    this.registerForm= this.fb.group({
      name     : ['',[Validators.required,Validators.maxLength(45)]],
      apellido : ['',[Validators.required,Validators.maxLength(45)]],
      email    : ['',[Validators.required,Validators.pattern(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}(?:\.[a-z]+)?$/i),
                      Validators.maxLength(254)],this.validadores.existeEmail.bind(this.validadores)],
      username : ['',[Validators.required,Validators.maxLength(30)],this.validadores.existeUsername.bind(this.validadores)],
      password : ['',[Validators.required,Validators.maxLength(15)]],
      password2: ['', [Validators.required]],
      student  : [true],
      teacher  : [false],
      admin    : [false]
    },
    {
      validators: this.passwordsIguales('password', 'password2'),
    } as FormControlOptions);

    // Suscribirse a los cambios en los checkboxes y manejar la lógica
    this.registerForm.get('student').valueChanges.subscribe((value: boolean) => {
      if (value) {
        this.registerForm.get('teacher').setValue(false);
        this.registerForm.get('admin').setValue(false);
      }else if(!this.registerForm.get('teacher').value && !this.registerForm.get('admin').value){
        this.registerForm.get('student').setValue(true);
      }
    });

    this.registerForm.get('teacher').valueChanges.subscribe((value: boolean) => {
      if (value) {
        this.registerForm.get('student').setValue(false);
      }else if(!this.registerForm.get('student').value && !this.registerForm.get('admin').value){
        this.registerForm.get('student').setValue(true);
      }
    });

    this.registerForm.get('admin').valueChanges.subscribe((value: boolean) => {
      if (value) {
        this.registerForm.get('student').setValue(false);
      }else if(!this.registerForm.get('student').value && !this.registerForm.get('teacher').value){
        this.registerForm.get('student').setValue(true);
      }
    });
  }

 
 /**===VALIDACIONES DE CAMPO PARA DEFINIR MENSAJE DE ERROR===**/
  isValido(param :string){
    return this.registerForm.get(param).invalid && (this.registerForm.get(param).dirty || this.registerForm.get(param).touched);
  }

  maxLength(param: string){
    return this.registerForm.get(param).hasError('maxlength');
  }

  pattern(param: string){
    return this.registerForm.get(param).hasError('pattern');
  }

  isEmail(param: string){
    return this.registerForm.get(param).hasError('existeEmail');
  }

  isUsername(param: string){
    return this.registerForm.get(param).hasError('existeUsername');
  }

  isPasswordsIguales(param: string){
    return this.registerForm.get(param).hasError('noEsIgual');
  }


  /**=== FIN VALIDACIONES DE CAMPO PARA DEFINIR MENSAJE DE ERROR===**/
  passwordsIguales(pass1Name : string, pass2Name : string){

      return (formGroup:FormGroup)=>{

        const pass1Control = formGroup.get(pass1Name);
        const pass2Control = formGroup.get(pass2Name);

        if(pass1Control.value===pass2Control.value){
          pass2Control.setErrors(null);
        }else{
          pass2Control.setErrors({noEsIgual:true});
        }
  
      };
    
  }

  /**=== POSTEO DE LA INFORMACIÓN === */
  crearUsuario() {
    
    
    if(this.registerForm.invalid || this.registerForm.pending){
        return Object.values(this.registerForm.controls).forEach(control=> {
        control.markAllAsTouched();
      });
    }
   // guardar información según modo y rol
    if(!this.modoEditar && !this.authService.hasRole(this.rolAdmin)){
      this.registrarUsuario();
      
    }else if (!this.modoEditar && this.authService.hasRole(this.rolAdmin)){
      this.crearUsuarioAdmin();
    }
    
  }

  crearUsuarioAdmin() {
    this.authService.create(this.registerForm.value).subscribe({
      next: (resp:any)=>{
        this.toast.success(resp.mensaje.detail,resp.mensaje.summary,{timeOut:duration});
        this.router.navigateByUrl('/dashboard/users/create');
      }
    });
    this.registerForm.reset();
  }

  registrarUsuario() {
    this.authService.register(this.registerForm.value).subscribe({
      next: (resp:any)=>{
        const mensaje= resp.mensaje.detail + ' Por favor inicie sesión con sus credenciales.';
        this.toast.success(mensaje,resp.mensaje.summary,{timeOut:duration});
        this.router.navigateByUrl('/login');
      }
    });
  }

 
}
