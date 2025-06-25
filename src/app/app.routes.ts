import { Routes } from '@angular/router';
import { RegisterComponent } from '../components/users/register/register.component';
import { LoginComponent } from '../components/users/login/login.component';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { AuthContentComponent } from '../auth/auth-content/auth-content.component';
import { CreateProductComponent } from '../components/products/create-product/create-product.component';
import { TabelCategoriesComponent } from '../components/products/tabel-categories/tabel-categories.component';
import { CreateCaregoryComponent } from '../components/products/create-caregory/create-caregory.component';
import { TabelProductsComponent } from '../components/products/tabel-products/tabel-products.component';
import { ViewProductComponent } from '../components/products/view-product/view-product.component';
import { ViewCartComponent } from '../components/cart/view-cart/view-cart.component';

export const routes: Routes = [
  { path: '', component: HeaderComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'auth', component: AuthContentComponent },
  { path: 'categories', component: TabelCategoriesComponent },
  { path: 'categories/create', component: CreateCaregoryComponent },
  { path: 'products/create', component: CreateProductComponent },
  { path: 'products', component: TabelProductsComponent },
  { path: 'categories/edit/:id', component: CreateCaregoryComponent },
  { path: 'products/edit/:id', component: CreateProductComponent },
  { path: 'products/view/:id', component: ViewProductComponent },
  { path: 'cart', component: ViewCartComponent },
];
