import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, pipe } from 'rxjs';
import { RegisterForm } from '../interfaces/register-form-interface';
import { RolEnum } from '../models/enums/RolEnum';
import { environment } from 'src/environments/environment.development';
import { MessagesService } from './messages.service';

const base_url_users = `${environment.base_url}/users`;

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient,
              private messages : MessagesService) { }

  register(formData: RegisterForm) {
    const { name: nombre, ...dataForm}= formData;
    const datosEnviar = { nombre, ...dataForm };

    return this.http.post(`${base_url_users}/register`,datosEnviar)
            .pipe(
              catchError(this.messages.errorHandler)
            );
  }

  create(formData: RegisterForm) {
    const { name: nombre, ...dataForm}= formData;
    const datosEnviar = { nombre, ...dataForm };

    return this.http.post(`${base_url_users}/create`,datosEnviar)
              .pipe(
                catchError(this.messages.errorHandler)
              );
  }

  editProfile(formData: RegisterForm, userId: number) {
    const { name: nombre, ...dataForm}= formData;
    const datosEnviar = { nombre, ...dataForm };

    return this.http.put(`${base_url_users}/edit/profile/${userId}`,datosEnviar)
              .pipe(
                catchError(this.messages.errorHandler)
              );
  }

  adminUserEdit(formData: RegisterForm, userId: number) {
    const { name: nombre, ...dataForm}= formData;
    const datosEnviar = { nombre, ...dataForm };

    return this.http.put(`${base_url_users}/edit/admin/${userId}`,datosEnviar)
              .pipe(
                catchError(this.messages.errorHandler)
              );
  }

  pageUsers(params: HttpParams) {
    return this.http.get(`${base_url_users}/all`,{params})
            .pipe(
              map((response:any)=>{
                const resp= response.negocio;
                resp.content.forEach((usuario: any) => {
                  usuario.roles = usuario.roles.map((rol: any) => {
                    const { id, ...resto } = rol;
                    const rolEnum: RolEnum = RolEnum[resto.rol];
                    return rolEnum;
                  });
                });   
                return resp;
              }),
              catchError(this.messages.errorHandler)
            )
  }

  findUSer(userId: number) {
    return this.http.get(`${base_url_users}/finduser/${userId}`)
              .pipe(
                map((response: any) =>{
                  const resp= response.negocio;
                  resp.roles = resp.roles.map((rol: any) => {
                    const { id, ...resto } = rol;
                    const rolEnum: RolEnum = RolEnum[resto.rol];
                    return rolEnum;
                  });   
                  return resp;
                }),
                catchError(this.messages.errorHandler)
              );
  }

  
  cambiarEstado(userId: number, estado: boolean) {
    return this.http.put(`${base_url_users}/estado/${userId}/${estado}`,null).
            pipe(
              catchError(this.messages.errorHandler)
            );
  }
}
