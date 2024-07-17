
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutedReportsFormComponent } from './executed-reports-form.component';

describe('ExecutedReportsFormComponent', () => {
  let component: ExecutedReportsFormComponent;
  let fixture: ComponentFixture<ExecutedReportsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExecutedReportsFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExecutedReportsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});