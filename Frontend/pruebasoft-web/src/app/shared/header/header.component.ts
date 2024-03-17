import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(public authService : AuthService,
              private toast: ToastrService,
              private router: Router) {
    
  }

  logout() {
        const username=this.authService.nombreApellido;
        this.authService.logout();
        this.toast.success(`${username}, has cerrado sesión con éxito!`, 'Logout');
        this.router.navigate(['/login']);
  }
}
