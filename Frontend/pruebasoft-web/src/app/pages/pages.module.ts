import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '../shared/shared.module';
import { AppRoutingModule } from '../app-routing.module';

import { PagesComponent } from './pages.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RegisterAdminComponent } from './register-admin/register-admin.component';
import { ComponentsModule } from "../components/components.module";
import { UsersProfileComponent } from './users/users-profile/users-profile.component';
import { PipesModule } from "../pipes/pipes.module";
import { PrimengModule } from '../primeng.module';
import { UsersListComponent } from './users/users-list/users-list.component';

@NgModule({
    declarations: [
        PagesComponent,
        DashboardComponent,
        RegisterAdminComponent,
        UsersProfileComponent,
        UsersListComponent
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
        ComponentsModule,
        PipesModule,
        PrimengModule
    ]
})
export class PagesModule { }
