import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../../app/material/material.module';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

export interface CartItem {
  userId: string;
  productId: string;
  quantity: number;
  product: {
    id: string;
    name: string;
    price: number;
    phoneModel: string;
    category: {
      id: string;
      name: string;
      description: string;
      deleted: boolean;
    };
    deleted: boolean;
    reviews: any[];
  };
}

@Component({
  selector: 'app-view-cart',
  imports: [ReactiveFormsModule, MaterialModule, FormsModule],
  templateUrl: './view-cart.component.html',
  styleUrl: './view-cart.component.css',
})
export class ViewCartComponent {
  cartItems: CartItem[] = [];
  userId = localStorage.getItem('userId') || '';

  constructor(private http: HttpClient, private router: Router) {}
  getCart(userId: string): void {
    this.http
      .get<CartItem[]>(`http://localhost:8080/api/cart/${userId}`)
      .subscribe({
        next: (data) => {
          this.cartItems = data;
        },
      });
  }

  ngOnInit(): void {
    this.getCart(this.userId);
  }

  onDelete(item: CartItem) {
    this.http
      .post(`http://localhost:8080/api/cart/remove`, {
        userId: this.userId,
        productId: item.product.id,
      })
      .subscribe({
        next: () => {
          this.cartItems = this.cartItems.filter(
            (cartItem) => cartItem.product.id !== item.product.id
          );
        },
        error: (err) => {},
      });
  }
  onBuy(item: CartItem) {
    /* ... */
  }

  incrementQuantity(item: any) {
    // Apelează backend-ul pentru a crește cantitatea
    this.http
      .post(`http://localhost:8080/api/cart/add`, {
        userId: this.userId,
        productId: item.product.id,
        quantity: 1, // Assuming a default quantity of 1})
      })
      .subscribe({
        next: () => {
          this.cartItems = this.cartItems.map((cartItem) => {
            if (cartItem.product.id === item.product.id) {
              return {
                ...cartItem,
                quantity: cartItem.quantity + 1, // Increment the quantity
              };
            }
            return cartItem;
          });
        },
        error: (err) => {},
      });
  }

  decrementQuantity(item: any) {
    this.http
      .post(`http://localhost:8080/api/cart/add`, {
        userId: this.userId,
        productId: item.product.id,
        quantity: -1, // Assuming a default quantity of 1})
      })
      .subscribe({
        next: () => {
          this.cartItems = this.cartItems.map((cartItem) => {
            if (cartItem.product.id === item.product.id) {
              return {
                ...cartItem,
                quantity: Math.max(cartItem.quantity - 1, 0), // Decrement the quantity but not below 0
              };
            }
            return cartItem;
          });
        },
        error: (err) => {
          console.error('Error adding product to cart', err);
        },
      });
  }
}
