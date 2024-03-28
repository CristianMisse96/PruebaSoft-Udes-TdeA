import { NgModule } from '@angular/core';


import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RegisterAdminComponent } from './register-admin/register-admin.component';
import { roleGuard } from '../guards/role.guard';
import { RolEnum } from '../models/enums/RolEnum';
import { UsersProfileComponent } from './users/users-profile/users-profile.component';
import { UsersListComponent } from './users/users-list/users-list.component';

const CHILD_ROUTES: Routes = [
  {path:'', component:DashboardComponent},
  {path:'users', component: UsersListComponent, canActivate:[roleGuard], data:{role : RolEnum.ROLE_ADMIN}},
  {path:'users/create', component: RegisterAdminComponent, canActivate:[roleGuard], data:{role : RolEnum.ROLE_ADMIN}},
  {path:'users/profile/:userId', component: UsersProfileComponent},
  
]

/**/
@NgModule({
  imports: [RouterModule.forChild(CHILD_ROUTES)],
  exports: [RouterModule]
})
export class ChildRoutesModule { }
