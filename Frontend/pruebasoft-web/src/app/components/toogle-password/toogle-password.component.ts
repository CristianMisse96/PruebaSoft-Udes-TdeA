import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-toogle-password',
  templateUrl: './toogle-password.component.html',
  styles: []
})
export class TooglePasswordComponent {

  @Input() hide = true;
  @Output() toggleVisibility = new EventEmitter<void>();

  togglePasswordVisibility(): void {
    this.toggleVisibility.emit();
  }
}
