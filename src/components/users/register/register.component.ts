import { Component, inject, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  NonNullableFormBuilder,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../../app/material/material.module';
import { NzFormTooltipIcon } from 'ng-zorro-antd/form';
import { first, Subject, takeUntil } from 'rxjs';
import { Router } from '@angular/router';
import { AxiosService } from '../../../axios/axios.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [ReactiveFormsModule, MaterialModule],
})
export class RegisterComponent implements OnInit {
  ngOnInit(): void {}
  private fb = inject(NonNullableFormBuilder);

  constructor(
    private http: HttpClient,
    private axiosService: AxiosService,
    private router: Router
  ) {}

  public message: string | null = null;

  userForm = this.fb.group({
    prenume: ['', [Validators.required]],
    nume: ['', [Validators.required]],
    username: ['', [Validators.required]],
    varsta: [null, [Validators.required, Validators.min(0)]],
    email: ['', [Validators.required, Validators.email]],
    parola: ['', [Validators.required, Validators.minLength(6)]],
  });

  submitForm(): void {
    if (this.userForm.valid) {
      var user = {
        firstName: this.userForm.value.prenume,
        lastName: this.userForm.value.nume,
        userName: this.userForm.value.username,
        age: this.userForm.value.varsta,
        email: this.userForm.value.email,
        password: this.userForm.value.parola,
      };

      this.message = null;
      // Send the user data to the backend for registration

      this.http
        .post<any>('http://localhost:8080/auth/register', user)
        .pipe(takeUntil(new Subject<void>()))
        .subscribe({
          next: (response) => {
            this.axiosService.setAuthToken(response.token);
            window.localStorage.setItem('userId', response.id);
            window.localStorage.setItem('userName', response.userName);
            window.localStorage.setItem('email', response.email);
            window.localStorage.setItem('admin', response.admin);
            this.router.navigate(['/']);
          },
          error: (error) => {
            this.message = error.error?.message || 'Eroare necunoscută!';
          },
        });

      // this.axiosService
      //   .request('post', '/register', user)
      //   .then((response) => {
      //     this.axiosService.setAuthToken(response.token);
      //     window.localStorage.setItem('userId', response.id);
      //     window.localStorage.setItem('userName', response.userName);
      //     window.localStorage.setItem('email', response.email);
      //   })
      //   .catch((error) => {
      //     console.error('Registration failed', error);
      //     // Handle error, e.g., show an error message
      //   });
    } else {
      Object.values(this.userForm.controls).forEach((control) => {
        control.markAsDirty();
        control.updateValueAndValidity({ onlySelf: true });
      });
    }
  }
}
