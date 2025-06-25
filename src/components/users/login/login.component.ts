import { Component, inject, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MaterialModule } from '../../../app/material/material.module';
import { HttpClient } from '@angular/common/http';
import { AxiosService } from '../../../axios/axios.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, MaterialModule],
})
export class LoginComponent implements OnInit {
  private fb = inject(NonNullableFormBuilder);

  constructor(
    private http: HttpClient,
    private axiosService: AxiosService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  submitForm(): void {
    if (this.loginForm.valid) {
      const user = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password,
      };
      debugger;
      this.http
        .post<any>('http://localhost:8080/auth/login', user)
        .pipe(takeUntil(new Subject<void>()))
        .subscribe({
          next: (response) => {
            this.axiosService.setAuthToken(response.token);
            window.localStorage.setItem('userId', response.id);
            window.localStorage.setItem('userName', response.userName);
            window.localStorage.setItem('email', response.email);
            window.localStorage.setItem('admin', response.admin);
            this.router.navigate(['/categories']);
          },
          error: (error) => {},
        });
    } else {
      Object.values(this.loginForm.controls).forEach((control) => {
        control.markAsDirty();
        control.updateValueAndValidity({ onlySelf: true });
      });
    }
  }
}
