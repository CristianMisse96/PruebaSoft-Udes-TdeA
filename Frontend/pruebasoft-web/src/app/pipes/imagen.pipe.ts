import { Pipe, PipeTransform } from '@angular/core';
import { environment } from 'src/environments/environment.development';

const base_url= environment.base_url;

@Pipe({
  name: 'imagen'
})
export class ImagenPipe implements PipeTransform {

  transform(img: string, tipo: 'usuario'): string {
    const endpoint= this.definirEndpoint(tipo);
    if(!img){
      return `${base_url}/${endpoint}/recurso/${tipo}/no-image`;
    }else if(img){
      return `${base_url}/${endpoint}/recurso/${tipo}/${img}`;
    }else{
      return `${base_url}/${endpoint}/recurso/${tipo}/no-image`;
    }
  }

  definirEndpoint(tipo: string): string {
    switch (tipo) {
      case 'usuario':
        return 'users';
        break;
    
      default:
        return '';
        break;
    }
  }

}
