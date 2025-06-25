import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { AxiosService } from '../../../axios/axios.service';
import { Router } from '@angular/router';
import { MaterialModule } from '../../../app/material/material.module';

interface Category {
  id: string;
  name: string;
}

@Component({
  selector: 'app-create-product',
  imports: [ReactiveFormsModule, MaterialModule, FormsModule],
  templateUrl: './create-product.component.html',
  styleUrl: './create-product.component.css',
})
export class CreateProductComponent {
  public phoneModels: string[] = ['SAMSUNG', 'IPHONE', 'MOTOROLA', 'XIAOMI'];
  public categories: Category[] = [];
  private id: string | null = null;

  private fb = inject(NonNullableFormBuilder);

  productForm = this.fb.group({
    name: ['', [Validators.required]],
    price: [null, [Validators.required, Validators.min(0)]],
    phoneModel: [null, [Validators.required]],
    categoryId: [null, [Validators.required]],
  });

  constructor(private http: HttpClient, private router: Router) {
    const url = this.router.url;
    if (url.includes('edit')) {
      const id = url.split('/').pop();
      this.id = String(id);
      this.getProductById(this.id);
    }
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.http
      .get<Category[]>('http://localhost:8080/api/categories')
      .pipe(takeUntil(new Subject<void>()))
      .subscribe({
        next: (data) => {
          this.categories = data.map((category) => ({
            id: category.id,
            name: category.name,
          }));
        },
      });
  }

  submitForm(): void {
    if (this.productForm.valid) {
      const product = this.productForm.value;

      if (this.id) {
        this.http
          .put(`http://localhost:8080/api/products/${this.id}`, product)
          .subscribe({
            next: () => {
              this.router.navigate(['/products']);
            },
            error: (err) => {
              console.error('Error updating product:', err);
            },
          });
      } else {
        this.http
          .post('http://localhost:8080/api/products', product)
          .subscribe({
            next: () => {
              this.router.navigate(['/products']);
            },
            error: (err) => {
              console.error('Error creating product:', err);
            },
          });
      }
    } else {
      Object.values(this.productForm.controls).forEach((control) => {
        control.markAsDirty();
        control.updateValueAndValidity({ onlySelf: true });
      });
    }
  }

  getProductById(id: string): void {
    this.http
      .get<any>(`http://localhost:8080/api/products/${id}`)
      .pipe(takeUntil(new Subject<void>()))
      .subscribe({
        next: (product) => {
          this.productForm.patchValue({
            name: product.name,
            price: product.price,
            phoneModel: product.phoneModel,
            categoryId: product.category.id,
          });
        },
        error: (error) => {
          console.error('Error fetching product:', error);
        },
      });
  }
}
