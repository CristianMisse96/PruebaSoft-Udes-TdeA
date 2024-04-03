import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RolEnum } from 'src/app/models/enums/RolEnum';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  rolAdmin: RolEnum= RolEnum.ROLE_ADMIN;
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
