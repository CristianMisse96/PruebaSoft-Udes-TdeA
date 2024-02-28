import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {FormControl} from '@angular/forms';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environments/environment.development';

const base_url= environment.base_url;

@Injectable({
  providedIn: 'root'
})
export class ValidadoresService {

  constructor( private http: HttpClient) {}

  /**
   * validación contra la base de datos para determinar si el correo ya existe debido a que es una llave unica
   * @param control control del furmulario con la información a validar
   * @returns error existeEmail en el control para su posterior validación en el form, true si existe algun error consumiendo el back
   */
  existeEmail(email: FormControl): Promise<ErrorValidate> | Observable<ErrorValidate>{
      
    if(!email.value){
      return Promise.resolve(null);
    }

    return new Promise((resolve)=>{
      this.http.get(`${base_url}/users/isemail/${email.value}`)
        .subscribe({
          next: (resp:any)=>{
            if(resp.negocio){
              resolve({existeEmail:true});
            }else{
              resolve(null);
            }
          },error:()=>{
            resolve({existeEmail:true});
          }
        })
    });   
  }

  existeUsername(username: FormControl):  Promise<ErrorValidate> | Observable<ErrorValidate>{

    if(!username.value){
      return Promise.resolve(null);
    }
    
    return new Promise((resolve)=>{
      this.http.get(`${base_url}/users/isusername/${username.value}`)
        .subscribe({
          next: (resp:any)=>{
            if(resp.negocio){
              resolve({existeUsername:true});
            }else{
              resolve(null);
            }
          },error:()=>{
            resolve({existeUsername:true});
          }
        })
    }); 
  }
}

interface ErrorValidate{
  [s:string] : boolean
}



