import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCaregoryComponent } from './create-caregory.component';

describe('CreateCaregoryComponent', () => {
  let component: CreateCaregoryComponent;
  let fixture: ComponentFixture<CreateCaregoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateCaregoryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateCaregoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
