import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabelProductsComponent } from './tabel-products.component';

describe('TabelProductsComponent', () => {
  let component: TabelProductsComponent;
  let fixture: ComponentFixture<TabelProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabelProductsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabelProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
