import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabelCategoriesComponent } from './tabel-categories.component';

describe('TabelCategoriesComponent', () => {
  let component: TabelCategoriesComponent;
  let fixture: ComponentFixture<TabelCategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabelCategoriesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabelCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
