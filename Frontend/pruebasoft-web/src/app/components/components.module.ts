import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TooglePasswordComponent } from './toogle-password/toogle-password.component';
import { RegisterFormComponent } from './register-form/register-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { DataEmptyComponent } from './data-empty/data-empty.component';
import { ModalImagenComponent } from './modal-imagen/modal-imagen.component';

@NgModule({
  declarations: [
    TooglePasswordComponent,
    RegisterFormComponent,
    DataEmptyComponent,
    ModalImagenComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  exports: [
    TooglePasswordComponent,
    RegisterFormComponent,
    DataEmptyComponent,
    ModalImagenComponent
  ]
})
export class ComponentsModule { }
