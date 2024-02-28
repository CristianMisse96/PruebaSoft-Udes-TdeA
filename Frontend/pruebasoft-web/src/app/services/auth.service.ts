import { Injectable } from '@angular/core';
import { LoginForm } from '../interfaces/login-form-interface';
import { environment } from 'src/environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { RolEnum } from '../models/enums/RolEnum';

const base_url = environment.base_url;

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  usuario: Usuario;
  private _token: string;
  constructor(private http: HttpClient) { }

  public get _usuario(){

    if(this.usuario!=null){
      return this.usuario
    }else if(this.usuario==null && localStorage.getItem('user')!=null){
      this.usuario= JSON.parse(localStorage.getItem('user')) as Usuario;
      return this.usuario;
    }

    return new Usuario('','','','',[],'','');
  }

  get token() {
    return localStorage.getItem('token');
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

          const{username,nombre,apellido,email,roles,foto,id}= resp.usuario;

          this.usuario= new Usuario(username,nombre,apellido,email,roles,'',foto,id);

          this.guardarLocalStorage(resp.token, this.usuario);

        })
      );
  }

  guardarLocalStorage(token:string, usuario: Usuario){

    const {password,email,...user}= usuario
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
  }

  hasRole(role : RolEnum) : boolean{
    if(this._usuario.roles.includes(role)){
      return true;
    }
    return false;
  }
}
