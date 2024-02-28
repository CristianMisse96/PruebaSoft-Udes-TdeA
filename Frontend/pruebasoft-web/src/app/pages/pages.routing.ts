import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { PagesComponent } from './pages.component';
import { authGuard } from '../guards/auth.guard';
//import { authGuard, canMatch } from '../guards/auth.guard';

const PAGES_ROUTES: Routes = [
    
    {path:'dashboard',
    component:PagesComponent,
    canActivate: [authGuard],
    //canMatch:[canMatch],
    loadChildren: ()=> import('./child-routes.module').then(m=> m.ChildRoutesModule),
  },
];

@NgModule({
    imports: [RouterModule.forChild(PAGES_ROUTES)],
    exports: [RouterModule]
})
export class PagesRoutingModule {}
