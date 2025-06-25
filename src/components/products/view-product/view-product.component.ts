import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MaterialModule } from '../../../app/material/material.module';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NzRateModule } from 'ng-zorro-antd/rate';

interface Review {
  id: string;
  userId: string;
  productId: string;
  rating: number;
  comment: string;
}

@Component({
  selector: 'app-view-product',
  imports: [ReactiveFormsModule, MaterialModule, FormsModule, NzRateModule],
  templateUrl: './view-product.component.html',
  styleUrl: './view-product.component.css',
})
export class ViewProductComponent {
  productId!: string;
  reviews: Review[] = [];
  submitting = false;
  errorMessage = '';

  private fb = inject(NonNullableFormBuilder);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  reviewForm = this.fb.group({
    rating: [1, [Validators.required, Validators.min(1), Validators.max(5)]],
    comment: ['', [Validators.required, Validators.maxLength(500)]],
  });

  ngOnInit(): void {
    this.productId = this.route.snapshot.paramMap.get('id')!;
    this.loadReviews();
  }

  loadReviews(): void {
    this.http
      .get<Review[]>(
        `http://localhost:8080/api/reviews/product/${this.productId}`
      )
      .subscribe({
        next: (data) => (this.reviews = data),
        error: () => (this.reviews = []),
      });
  }

  submitReview(): void {
    if (this.reviewForm.invalid) {
      this.reviewForm.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.errorMessage = '';
    const userId = localStorage.getItem('userId'); // sau din AuthService
    const review = {
      ...this.reviewForm.value,
      productId: this.productId,
      userId: userId,
    };
    this.http.post('http://localhost:8080/api/reviews', review).subscribe({
      next: () => {
        this.reviewForm.reset({ rating: 5, comment: '' });
        this.loadReviews();
        this.submitting = false;
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || 'Eroare la trimiterea review-ului!';
        this.submitting = false;
      },
    });
  }
}
