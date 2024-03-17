import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  constructor(private router: Router) {}

    /** Metodo para crear dialog con error de conexión con el servicio
     * @param error variable que muestra que exite un error de conexión
     */
    errorHandler(error: HttpErrorResponse) {
        const TITULO_SIN_CONEXION = '¡Sin conexión!';
        const MENSAJE_SIN_CONEXION =
            'No se ha podido establecer una conexión con el servidor, por favor intente mas tarde.';
        const TITULO_ERROR_PETICION = 'Falla técnica';
        const MENSAJE_ERROR_PETICION =
            'Ocurrió un error procesando su solicitud o su sesión ha caducado, ingrese nuevamente.';

        if (error.status === 0) {

            Swal.fire({
                html:
                    '<h3>' +
                    TITULO_SIN_CONEXION +
                    '</h3><p class="text-dialog">' +
                    MENSAJE_SIN_CONEXION +
                    '</p>',
                imageUrl: 'assets/images/icono-sin-conexion.svg',
                confirmButtonText: 'Aceptar',
                showCloseButton: true
            });
        } else if(error.status != 403 && error.status != 401){
            const titulo = error.error.mensaje ? error.error.mensaje.summary : TITULO_ERROR_PETICION;
            const mensaje = error.error.mensaje ? error.error.mensaje.detail : MENSAJE_ERROR_PETICION;
            Swal.fire({
                html: '<h3>' + titulo + '</h3><p class="text-dialog">' + mensaje + '</p>',
                icon: 'error',
                confirmButtonText: 'Aceptar',
                showCloseButton: true
            });
        }
        return throwError(()=> error?.error?.mensaje || 'server error.');
    }
}
