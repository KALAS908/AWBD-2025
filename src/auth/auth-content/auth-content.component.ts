// src/app/components/auth-content/auth-content.component.ts
import { Component, OnInit } from '@angular/core';
import { AxiosService } from '../../axios/axios.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-auth-content',
  templateUrl: './auth-content.component.html',
  styleUrls: ['./auth-content.component.css'],
})
export class AuthContentComponent implements OnInit {
  data: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<string[]>('http://localhost:8080/api/users').subscribe({
      next: (data) => {
        this.data = data;
        console.log('Data:', this.data);
      },
      error: (err) => console.error('Error:', err),
    });
  }
}
