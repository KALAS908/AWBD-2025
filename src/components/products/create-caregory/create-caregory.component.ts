import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { AxiosService } from '../../../axios/axios.service';
import { Router } from '@angular/router';
import { MaterialModule } from '../../../app/material/material.module';

@Component({
  selector: 'app-create-caregory',
  imports: [ReactiveFormsModule, MaterialModule],
  templateUrl: './create-caregory.component.html',
  styleUrl: './create-caregory.component.css',
})
export class CreateCaregoryComponent {
  private fb = inject(NonNullableFormBuilder);

  public id: string | null = null;

  categoryForm = this.fb.group({
    name: ['', [Validators.required]],
    description: ['', [Validators.required]],
  });

  constructor(
    private http: HttpClient,
    private axiosService: AxiosService,
    private router: Router
  ) {
    const url = this.router.url;
    if (url.includes('edit')) {
      const id = url.split('/').pop();
      this.id = String(id);
      this.getCategoryById(this.id);
    }
  }

  submitForm(): void {
    if (this.categoryForm.valid) {
      var category = {
        name: this.categoryForm.value.name,
        description: this.categoryForm.value.description,
      };

      if (this.id) {
        this.http
          .put<any>(`http://localhost:8080/api/categories/${this.id}`, category)
          .pipe(takeUntil(new Subject<void>()))
          .subscribe({
            next: (response) => {
              this.router.navigate(['/categories']);
            },
            error: (error) => {},
          });
      } else {
        this.http
          .post<any>('http://localhost:8080/api/categories', category)
          .pipe(takeUntil(new Subject<void>()))
          .subscribe({
            next: (response) => {
              this.router.navigate(['/categories']);
            },
            error: (error) => {},
          });
      }
    } else {
      Object.values(this.categoryForm.controls).forEach((control) => {
        control.markAsDirty();
        control.updateValueAndValidity({ onlySelf: true });
      });
    }
  }

  getCategoryById(id: string): void {
    this.http
      .get<any>(`http://localhost:8080/api/categories/${id}`)
      .pipe(takeUntil(new Subject<void>()))
      .subscribe({
        next: (response) => {
          this.categoryForm.patchValue({
            name: response.name,
            description: response.description,
          });
        },
        error: (error) => {
          console.error('Error fetching category:', error);
        },
      });
  }
}
