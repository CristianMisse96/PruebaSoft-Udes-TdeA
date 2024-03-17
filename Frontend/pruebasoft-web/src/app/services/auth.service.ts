import { Injectable } from '@angular/core';
import { LoginForm } from '../interfaces/login-form-interface';
import { environment } from 'src/environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { catchError, tap } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { RolEnum } from '../models/enums/RolEnum';
import { RegisterForm } from '../interfaces/register-form-interface';
import { MessagesService } from './messages.service';

const base_url = environment.base_url;

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  usuario: Usuario;
 
  constructor(private http: HttpClient,
              private messages : MessagesService) { }

  public get _usuario(){

    if(this.usuario!=null){
      return this.usuario
    }else if(this.usuario==null && this.obtenerDatosToken(this.token)!=null){
      let payload= this.obtenerDatosToken(this.token);
      this.usuario= new Usuario(payload.username,payload.nombre,payload.apellido,payload.foto,payload.id,payload.email,payload.authorities);
      return this.usuario;
    }

    return new Usuario('','','');
  }

  get token() {
    return localStorage.getItem('tokenPruebaSoft');
  }


  isAuthenticathed(): boolean {
    let payload = this.obtenerDatosToken(this.token);

    if (payload?.username) {
      return payload.username.length > 0;
    }

    return false;

  }

  obtenerDatosToken(accessToken: string): any {

    if (accessToken != null) {
      return JSON.parse(atob(accessToken.split(".")[1]));
    }

    return null;
  }

  login(formData: LoginForm) {
    const { remember, ...credentialsWithoutRemember } = formData;
    return this.http.post(`${base_url}/login`, credentialsWithoutRemember)
      .pipe(
        tap((resp: any) => {
          const {nombre,apellido,foto}= resp.usuario;
          const user= new Usuario(null,nombre,apellido,foto);
          this.guardarLocalStorage(resp.token, user);

        })
      );
  }

  guardarLocalStorage(token:string, usuario: Usuario){
    localStorage.setItem('tokenPruebaSoft', token);
    localStorage.setItem('userPruebasoft',JSON.stringify(usuario));
  }

  hasRole(role : RolEnum) : boolean{
    
    return this._usuario.roles?.includes(role) || false;

  }

  logout(){
    this.usuario=null;
    localStorage.removeItem('tokenPruebaSoft');
    localStorage.removeItem('userPruebaSoft');
  }

  register(formData: RegisterForm) {
    const { name: nombre, ...dataForm}= formData;
    const datosEnviar = { nombre, ...dataForm };

    return this.http.post(`${base_url}/users/register`,datosEnviar)
            .pipe(
              catchError(this.messages.errorHandler)
            );
  }

  create(formData: RegisterForm) {
    const { name: nombre, ...dataForm}= formData;
    const datosEnviar = { nombre, ...dataForm };

    return this.http.post(`${base_url}/users/create`,datosEnviar)
              .pipe(
                catchError(this.messages.errorHandler)
              );
  }

  isTokenExpirado() : boolean{
    const token = this.token;
    const payload = this.obtenerDatosToken(token) ;
    const nowInSeconds = Math.floor(Date.now() / 1000);

    return !payload || payload.exp < nowInSeconds;
  }

  get nombreApellido(){
    const usuarioString = localStorage.getItem('userPruebasoft');
    if (!usuarioString) {
      return 'Sin Nombre';
    }

    const usuario = JSON.parse(usuarioString);
    const fullname= `${usuario.nombre.split(' ')[0]} ${usuario.apellido.split(' ')[0]}`;

    return fullname;
  }
}
