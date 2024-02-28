import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styles: [
  ]
})
export class FooterComponent {
  
  autor: string = 'Manda Software';
  texto: string= 'PruebaSoft UDES-TDEA. Todos los derechos reservados.'
  currentYear: number = new Date().getFullYear();
}
