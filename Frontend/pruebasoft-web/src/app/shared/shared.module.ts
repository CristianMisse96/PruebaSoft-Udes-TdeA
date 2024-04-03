import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { PipesModule } from "../pipes/pipes.module";
import { RouterModule } from '@angular/router';



@NgModule({
    declarations: [
        FooterComponent,
        HeaderComponent
    ],
    exports: [
        FooterComponent,
        HeaderComponent
    ],
    imports: [
        CommonModule,
        PipesModule,
        RouterModule
    ]
})
export class SharedModule { }
