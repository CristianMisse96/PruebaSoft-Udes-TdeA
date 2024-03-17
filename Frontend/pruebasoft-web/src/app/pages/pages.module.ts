import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '../shared/shared.module';
import { AppRoutingModule } from '../app-routing.module';

import { PagesComponent } from './pages.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RegisterAdminComponent } from './register-admin/register-admin.component';
import { ComponentsModule } from "../components/components.module";




@NgModule({
    declarations: [
        PagesComponent,
        DashboardComponent,
        RegisterAdminComponent,
    ],
    exports: [
        PagesComponent,
        DashboardComponent,
        RegisterAdminComponent,
    ],
    imports: [
        CommonModule,
        SharedModule,
        AppRoutingModule,
        ComponentsModule
    ]
})
export class PagesModule { }
