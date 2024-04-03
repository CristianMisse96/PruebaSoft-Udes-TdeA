import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { ToastrModule } from 'ngx-toastr';
import { NoPageFoundComponent } from './no-page-found/no-page-found.component';
import { AppRoutingModule } from './app-routing.module';
import { AuthModule } from './auth/auth.module';
import { PagesModule } from './pages/pages.module';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { SharedModule } from "./shared/shared.module";


@NgModule({
    declarations: [
        AppComponent,
        NoPageFoundComponent
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    ],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot(),
        AppRoutingModule,
        PagesModule,
        AuthModule,
        HttpClientModule,
        SharedModule
    ]
})
export class AppModule { }
