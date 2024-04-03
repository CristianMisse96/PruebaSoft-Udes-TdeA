import { HttpParams } from '@angular/common/http';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Usuario } from 'src/app/models/usuario.model';
import { AuthService } from 'src/app/services/auth.service';
import { Table, TableLazyLoadEvent } from 'primeng/table';
import { ModalImagenService } from 'src/app/services/modal-imagen.service';
import { Subscription, delay } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styles: [
  ]
})
export class UsersListComponent implements OnInit, OnDestroy{

  @ViewChild('dt2') dt2: Table;
  editarUsuario: boolean=false;
  userEdit: Usuario;
  ICON_EMPTY = 'no-resultados.svg';
  TITLE_EMPTY='Sin información.';
  CONTENT_EMPTY='No existe contenido para visualizar.'
  //Manejo programático del paginador
   first = 0;
  //Página actual
   pagina = 0;
   totalRegistros: number=0;
   lazyLoadEvent: any; 
   imgSubs: Subscription;
   users: Usuario[]= [];

  constructor(public authService: AuthService,
              private modalImagenService: ModalImagenService,
              private toast: ToastrService,
              private router: Router,
              private userService: UserService) {
    
  }

  ngOnInit(): void {
    this.usuariosPaginados('10', '0');

    this.imgSubs= this.modalImagenService.nuevaImagen
    .pipe(delay(100))
    .subscribe(img=> this.usuariosPaginados('10', '0'));
  }

  ngOnDestroy(): void {
    this.imgSubs.unsubscribe();
  }

  usuariosPaginados(rows: string, paginaProceso: string, sortField?: string
    , sortOrder?: number, filtros?: string) {
    
    let params = new HttpParams();
        params = params.append('cantRegPorPagina', rows);
        params = params.append('numPagina', paginaProceso);
        if (sortField !== undefined) {
          params = params.append('sortField', sortField);
      }
      if (sortOrder !== undefined) {
          params = params.append('sortOrder', sortOrder.toString());
      }
      if(filtros){
        params = params.append('filtros', filtros);
      }
    this.cargarRegistros(params);
  }

  cargarRegistros(parametros: HttpParams) {
    this.userService.pageUsers(parametros).subscribe({
      next: (resp)=>{console.log(resp);
        this.users= resp.content as Usuario[];
        this.totalRegistros= resp.totalElements;
      }
    });
  }

  lazyLoadTable(event: TableLazyLoadEvent) {console.log(event);
    
    this.lazyLoadEvent =event;
      this.pagina = event.first / event.rows;
      this.first = event.first;
      const sortField = event.sortField !== undefined ? event.sortField.toString() : undefined;
      const filtros = event?.globalFilter? event.globalFilter.toString() : undefined;
      this.usuariosPaginados(event.rows.toString(),
                            this.pagina.toString(),
                            sortField,
                            event.sortOrder,
                            filtros);
  
  }

  obtenerIconoEstado(estado: boolean): string {
    switch (estado) {
        case true:
            return 'success';
        case false:
            return 'error';
        default:
            return 'disabled';
    }
  }

  abrirModal(id: number, img: string) {
    this.modalImagenService.abrirModal('users',id,img);
  }

  activar(user: Usuario) {
      Swal.fire({
        title: "Activar usuario!!!",
        text: `Esta a punto activar al usuario ${user.username}`,
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "si, activar!"
      }).then((result) => {
        if (result.isConfirmed) {
          this.userService.cambiarEstado(user.id,true).subscribe({
            next: ()=>{
              this.toast.success(`Usuario ${user.username} activado correctamente`,
              'Proceso exitoso',{timeOut: 2000});
              this.usuariosPaginados('10', '0');
            },
          });
        }
      });
  }

  desactivar(user: Usuario) {
    if(user.id===  this.authService._usuario.id){
      this.toast.info('No puede inactivar su propia cuenta','Alerta!!!',{timeOut: 2000});
      this.router.navigateByUrl('/dashboard/users'); 
      return;
    }

    Swal.fire({
      title: "Inactivar usuario!!!",
      text: `Esta a punto inactivar al usuario ${user.username}`,
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "si, inactivar!"
    }).then((result) => {
      if (result.isConfirmed) {
        this.userService.cambiarEstado(user.id,false).subscribe({
          next: ()=>{
            this.toast.success(`Usuario ${user.username} inactivado correctamente`,
            'Proceso exitoso',{timeOut: 2000});
            this.usuariosPaginados('10', '0');
          },
        });
      }
    });

  }

  modoEditar(userEdit: Usuario) {
    this.userEdit= userEdit;
    this.userEdit.password= 'Sin pass';
    this.editarUsuario=true;
  }

}
