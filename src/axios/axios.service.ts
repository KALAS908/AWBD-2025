import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import axios from 'axios';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AxiosService {
  public isLoggedIn$ = new BehaviorSubject<boolean>(false);
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isLoggedIn$.next(this.getAuthToken() != null);
    axios.defaults.baseURL = 'http://localhost:8080/auth';
    axios.defaults.headers.post['Content-Type'] = 'application/json';
  }
  getAuthToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return window.localStorage.getItem('authToken');
    }
    return null;
  }

  setAuthToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      if (token != null) {
        window.localStorage.setItem('authToken', token);
      } else {
        window.localStorage.removeItem('authToken');
      }
      // Actualizează starea
      this.isLoggedIn$.next(this.getAuthToken() != null);
    }
  }

  request(method: string, url: string, data: any): Promise<any> {
    let headers = {};

    const token = this.getAuthToken();
    if (token != null) {
      headers = {
        Authorization: `Bearer ${token}`,
      };
    }

    return axios({
      method: method,
      url: url,
      data: data,
      headers: headers,
    }).then((response) => response.data);
  }
}
