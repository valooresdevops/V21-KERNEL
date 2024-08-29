import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertBuilderComponent } from './alert-builder.component';

describe('AlertBuilderComponent', () => {
  let component: AlertBuilderComponent;
  let fixture: ComponentFixture<AlertBuilderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertBuilderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AlertBuilderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
