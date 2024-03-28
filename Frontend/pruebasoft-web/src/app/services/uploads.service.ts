import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, tap } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { MessagesService } from './messages.service';
import { Usuario } from '../models/usuario.model';

const base_url= environment.base_url;
@Injectable({
  providedIn: 'root'
})
export class UploadsService {

  constructor(private http: HttpClient,
              private messages : MessagesService) { }

  subirFoto(recurso : File,endpoint:'users', id, isProfile: boolean){

    let formData = new FormData();
    formData.append("archivo",recurso);
    formData.append("id",id);

    return this.http.post(`${base_url}/${endpoint}/upload`,formData)
            .pipe(
              tap((resp: any) => {
                if(isProfile){
                  const {nombre,apellido,foto,id}= resp.negocio;
                  const user= new Usuario(null,nombre,apellido,foto);
                  localStorage.setItem('userPruebaSoft', JSON.stringify(user));
                }
              }),
              catchError(this.messages.errorHandler)
            );
  }
}
