import { NgModule } from '@angular/core';


import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';

const CHILD_ROUTES: Routes = [
  {path:'', component:DashboardComponent},
]

@NgModule({
  imports: [RouterModule.forChild(CHILD_ROUTES)],
  exports: [RouterModule]
})
export class ChildRoutesModule { }
