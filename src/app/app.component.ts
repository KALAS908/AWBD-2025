import { afterNextRender, Component, Inject, PLATFORM_ID } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from './material/material.module';
import { AxiosService } from '../axios/axios.service';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [MaterialModule, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  public isLoggedIn: boolean = false;
  public userName: string | null = '';
  public isAdmin: boolean = false;

  constructor(
    private router: Router,
    private axiosService: AxiosService,
    @Inject(PLATFORM_ID) private platformId: any
  ) {
    afterNextRender(() => {
      this.updateLoginStatus();
      // Mutăm inițializarea aici pentru SSR
      this.userName = localStorage.getItem('userName') || null;
      this.isAdmin = localStorage.getItem('admin') === 'true';
    });
  }

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.router.events.subscribe(() => {
        this.updateLoginStatus();
      });
      this.router.navigate(['/']);
    }
  }

  private updateLoginStatus(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.isLoggedIn = this.axiosService.getAuthToken() != null;
    }
  }

  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('userId');
      localStorage.removeItem('userName');
      localStorage.removeItem('email');
      localStorage.removeItem('admin');
    }
    this.isLoggedIn = false;
    this.userName = null;
    this.router.navigate(['/login']);
  }
}
