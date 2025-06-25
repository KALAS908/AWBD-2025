import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../../app/material/material.module';
import { HttpClient } from '@angular/common/http';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzPaginationModule } from 'ng-zorro-antd/pagination';
import { NzInputModule } from 'ng-zorro-antd/input';
import { Subject, takeUntil } from 'rxjs';
import { Router } from '@angular/router';

interface Category {
  id: string;
  name: string;
  description: string;
}

@Component({
  selector: 'app-tabel-categories',
  imports: [ReactiveFormsModule, MaterialModule, FormsModule],
  templateUrl: './tabel-categories.component.html',
  styleUrl: './tabel-categories.component.css',
})
export class TabelCategoriesComponent {
  categories: Category[] = [];
  filteredCategories: Category[] = [];
  searchValue: string = '';
  pageIndex: number = 1;
  pageSize: number = 5;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.http
      .get<Category[]>('http://localhost:8080/api/categories')
      .pipe(takeUntil(new Subject<void>()))
      .subscribe({
        next: (data) => {
          this.categories = data;
          this.applyFilter();
        },
      });
  }

  applyFilter(): void {
    const value = this.searchValue.trim().toLowerCase();
    this.filteredCategories = this.categories.filter(
      (cat) =>
        cat.name.toLowerCase().includes(value) ||
        cat.description.toLowerCase().includes(value)
    );
    this.pageIndex = 1;
  }

  onPageIndexChange(newPage: number): void {
    this.pageIndex = newPage;
  }

  get pagedCategories(): Category[] {
    const start = (this.pageIndex - 1) * this.pageSize;
    return this.filteredCategories.slice(start, start + this.pageSize);
  }

  onEdit(cat: Category): void {
    this.router.navigate(['/categories/edit', cat.id]);
  }

  onDelete(cat: Category): void {
    this.http
      .delete(`http://localhost:8080/api/categories/${cat.id}`)
      .pipe(takeUntil(new Subject<void>()))
      .subscribe({
        next: () => {
          this.categories = this.categories.filter((c) => c.id !== cat.id);
          this.applyFilter();
        },
        error: (error) => {
          console.error('Eroare la ștergere:', error);
        },
      });
  }
}
