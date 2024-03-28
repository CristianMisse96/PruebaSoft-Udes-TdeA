import { EventEmitter, Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.development';

const base_url= environment.base_url;

@Injectable({
  providedIn: 'root'
})
export class ModalImagenService {

  private _ocultarModal:boolean = true;
  tipo: 'users';
  id:number;
  img:string;
  nuevaImagen: EventEmitter<string>= new EventEmitter<string>();

  constructor() { }

  get ocultarModal(){
    return this._ocultarModal;
  }

  abrirModal(tipo: 'users', 
            id:number,
            img: string='no-img'){
   
    this._ocultarModal=false;debugger;
    this.tipo=tipo;
    this.id=id;
    const endpoint= this.definirEndpoint(tipo);
    this.img= `${base_url}/${tipo}/recurso/${endpoint}/${img}`;
    
  }

  definirEndpoint(tipo: string) {
    switch (tipo) {
      case 'users':
        return 'usuario';
      default:
        return 'usuario';
        
    }
  }

  cerrarModal(){
    this._ocultarModal=true;
  }

}
