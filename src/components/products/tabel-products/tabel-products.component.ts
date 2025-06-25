import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../../app/material/material.module';
import { Router } from '@angular/router';

interface Product {
  id: string;
  name: string;
  price: number;
  phoneModel: string;
  category: { id: string; name: string };
}

@Component({
  selector: 'app-tabel-products',
  imports: [ReactiveFormsModule, MaterialModule, FormsModule],
  templateUrl: './tabel-products.component.html',
  styleUrl: './tabel-products.component.css',
})
export class TabelProductsComponent {
  products: Product[] = [];
  pageIndex: number = 1;
  pageSize: number = 5;
  userId = localStorage.getItem('userId') || '';
  public isAdmin: boolean = false; // Set this based on your authentication logic

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadProducts();
    this.isAdmin = localStorage.getItem('admin') === 'true'; // Verifică dacă utilizatorul este admin
  }

  loadProducts(): void {
    this.http.get<Product[]>('http://localhost:8080/api/products').subscribe({
      next: (data) => {
        this.products = data;
      },
    });
  }

  get pagedProducts(): Product[] {
    const start = (this.pageIndex - 1) * this.pageSize;
    return this.products.slice(start, start + this.pageSize);
  }

  onPageIndexChange(newPage: number): void {
    this.pageIndex = newPage;
  }

  onEdit(prod: Product): void {
    this.router.navigate(['/products/edit', prod.id]);
  }

  onDelete(prod: Product): void {
    this.http
      .delete(`http://localhost:8080/api/products/${prod.id}`)
      .subscribe({
        next: () => {
          this.products = this.products.filter((p) => p.id !== prod.id);
        },
        // Handle error if needed
      });
    console.log('Delete', prod);
  }

  onBuy(prod: Product): void {
    if (this.userId) {
      this.http
        .post(`http://localhost:8080/api/cart/add`, {
          userId: this.userId,
          productId: prod.id,
          quantity: 1, // Assuming a default quantity of 1})
        })
        .subscribe({
          next: () => {
            console.log('Product added to cart successfully');
          },
          error: (err) => {
            console.error('Error adding product to cart', err);
          },
        });
    } else {
      console.error('User not logged in');
    }
  }

  onView(prod: Product): void {
    this.router.navigate(['/products/view', prod.id]);
  }
}
