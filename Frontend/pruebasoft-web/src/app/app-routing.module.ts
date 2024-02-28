import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PagesRoutingModule } from './pages/pages.routing';
import { AuthRoutingModule } from './auth/auth-routing';

import { NoPageFoundComponent } from './no-page-found/no-page-found.component';




const MAIN_ROUTES : Routes=[

  {path:'', pathMatch:'full', redirectTo:'dashboard'},
  {path:'**', component: NoPageFoundComponent},
]

@NgModule({
  imports: [
    RouterModule.forRoot(MAIN_ROUTES),
    PagesRoutingModule,
    AuthRoutingModule
  ],
  exports:[
    RouterModule
  ]
})
export class AppRoutingModule { }
