
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutedReportsComponent } from './executed-reports.component';

describe('ExecutedReportsComponent', () => {
  let component: ExecutedReportsComponent;
  let fixture: ComponentFixture<ExecutedReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExecutedReportsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExecutedReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});