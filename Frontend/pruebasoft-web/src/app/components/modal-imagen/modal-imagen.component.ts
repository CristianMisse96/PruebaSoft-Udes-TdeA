import { Component, ElementRef } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ModalImagenService } from 'src/app/services/modal-imagen.service';
import { UploadsService } from 'src/app/services/uploads.service';
//import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-modal-imagen',
  templateUrl: './modal-imagen.component.html',
  styles: [
  ]
})
export class ModalImagenComponent {

  imagenSubir: File;
  imgTemp: string | ArrayBuffer;

  constructor(public modalImagenService: ModalImagenService,
              public fileUploadService: UploadsService,
              private toast: ToastrService,
              private elementRef: ElementRef) {
    
  }

  cerrarModal(){
    this.imgTemp=null;
    this.modalImagenService.cerrarModal();
  }

  cambiarImagen($event) {
    const file :File= $event.target.files[0]
     this.imagenSubir=file;
 
     if(!file){
      this.imgTemp = null;
      return;
    }
     if(this.imagenSubir.type.indexOf('image') < 0){
      this.toast.error('El archivo debe ser de tipo imagen', 'Error al seleccionar imagen',{timeOut:2000});
      this.imgTemp = null;
      this.resetearInput();
      this.imagenSubir=null;
      return;
    }
 
     const reader= new FileReader();
     reader.readAsDataURL(file);
 
     reader.onloadend= ()=>{
       this.imgTemp= reader.result;
     }
  }

   subirImagen(){
    if(!this.imagenSubir){
      this.toast.error('Debe seleccionar una foto','Error!!!' ,{timeOut:2500});
      return;
    }
    const id= this.modalImagenService.id;
    const tipo= this.modalImagenService.tipo;

    this.fileUploadService.subirFoto(this.imagenSubir,tipo,id,false)
      .subscribe({
        next: (resp:any)=>{
          this.toast.success(resp.mensaje.detail,resp.mensaje.summary,{timeOut:2500});
          this.modalImagenService.nuevaImagen.emit(resp.negocio.foto);
          this.cerrarModal();
        }
      });
  }

  private resetearInput() {
    const fileInput = this.elementRef.nativeElement.querySelector('input[type="file"]');
    if (fileInput?.files?.length > 0) {
      fileInput.value = '';
    }
  }

}


