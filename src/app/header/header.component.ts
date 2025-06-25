import { Component, Input } from '@angular/core';
import { MaterialModule } from '../material/material.module';

@Component({
  selector: 'app-header',
  imports: [MaterialModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  @Input() pageTitle?: string = 'Store Front';
}
