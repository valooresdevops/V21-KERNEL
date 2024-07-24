import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateExecutedReportComponent } from './create-executed-report.component';

describe('CreateExecutedReportComponent', () => {
  let component: CreateExecutedReportComponent;
  let fixture: ComponentFixture<CreateExecutedReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateExecutedReportComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateExecutedReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
