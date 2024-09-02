import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertBuilderFormComponent } from './alert-builder-form.component';

describe('AlertBuilderFormComponent', () => {
  let component: AlertBuilderFormComponent;
  let fixture: ComponentFixture<AlertBuilderFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertBuilderFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AlertBuilderFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
