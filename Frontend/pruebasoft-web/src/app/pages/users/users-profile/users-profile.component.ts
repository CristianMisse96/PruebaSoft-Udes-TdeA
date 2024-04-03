import { Location } from '@angular/common';
import { Component, ElementRef, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Usuario } from 'src/app/models/usuario.model';
import { AuthService } from 'src/app/services/auth.service';
import { UploadsService } from 'src/app/services/uploads.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-users-profile',
  templateUrl: './users-profile.component.html',
  styles: [
  ]
})
export class UsersProfileComponent implements OnInit{


  usuario: Usuario;
  imgTemp: string | ArrayBuffer;
  imagenSubir: File;

  constructor(public authService: AuthService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private toast: ToastrService,
              private uplodaService: UploadsService,
              private elementRef: ElementRef,
              private location: Location,
              private userService: UserService){}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(({userId})=>{
      const userIdNumber = +userId; 
      this.cargarUsuario(userIdNumber);
    });
  }

  cargarUsuario(userId: number) {
    if(userId !== this.authService._usuario.id){
      this.toast.info('Solo puedes editar tu propio perfil','Alerta!!!',{timeOut: 2000});
      this.router.navigateByUrl('/'); return;
    }
      this.userService.findUSer(userId).subscribe({
          next: (resp: any)=> {
            const{apellido,email,nombre,foto,roles,username} = resp;
            this.usuario=new Usuario(username,nombre,apellido,foto,userId,email,roles,'Sin Pass');
          },
      });
    
  }

  cambiarImagen($event) {
    const file :File= $event.target.files[0]
     this.imagenSubir=file;
 
     if(!file){
      this.imgTemp = null;
      return;
    }
     if(this.imagenSubir.type.indexOf('image') < 0){
      this.toast.error('El archivo debe ser de tipo imagen', 'Error al seleccionar imagen',{timeOut:2000});
      this.imgTemp = null;
      this.resetearInput();
      this.imagenSubir=null;
      return;
    }
 
     const reader= new FileReader();
     reader.readAsDataURL(file);
 
     reader.onloadend= ()=>{
       this.imgTemp= reader.result;
     }
  }

  subirImagen() {
    if(!this.imagenSubir){
      this.toast.error('Debe seleccionar una foto','Error!!!' ,{timeOut:2500});
    }else{
      this.uplodaService.subirFoto(this.imagenSubir,'users',this.usuario.id,true).subscribe({
          next: (resp:any)=>{
            this.toast.success(resp.mensaje.detail,resp.mensaje.summary,{timeOut:2500});
            this.router.navigateByUrl(`/dashboard/users/profile/${this.usuario.id}`);
            this.imagenSubir=null;
            this.resetearInput();
          },
      })
    }
  }

  onRegresar() {
    this.location.back();
  }


  private resetearInput() {debugger;
    const fileInput = this.elementRef.nativeElement.querySelector('input[type="file"]');
    if (fileInput?.files?.length > 0) {
      fileInput.value = '';
    }
  }
}
