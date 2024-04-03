import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { AvatarModule } from 'primeng/avatar';

@NgModule({
  declarations: [],
  exports: [
    TableModule,
    ButtonModule,
    DropdownModule,
    AvatarModule
  ],imports:[
    BrowserModule,
    BrowserAnimationsModule,
  ]
})
export class PrimengModule{

}
