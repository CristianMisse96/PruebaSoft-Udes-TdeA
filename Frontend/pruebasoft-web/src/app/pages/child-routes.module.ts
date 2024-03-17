import { NgModule } from '@angular/core';


import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RegisterAdminComponent } from './register-admin/register-admin.component';
import { roleGuard } from '../guards/role.guard';
import { RolEnum } from '../models/enums/RolEnum';

const CHILD_ROUTES: Routes = [
  {path:'', component:DashboardComponent},
  {path:'users/create', component: RegisterAdminComponent, canActivate:[roleGuard], data:{role : RolEnum.ROLE_ADMIN}}
]

/**/
@NgModule({
  imports: [RouterModule.forChild(CHILD_ROUTES)],
  exports: [RouterModule]
})
export class ChildRoutesModule { }
