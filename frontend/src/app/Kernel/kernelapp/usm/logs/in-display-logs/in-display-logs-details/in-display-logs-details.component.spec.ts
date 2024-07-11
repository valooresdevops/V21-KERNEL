import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InDisplayLogsDetailsComponent } from './in-display-logs-details.component';

describe('InDisplayLogsDetailsComponent', () => {
  let component: InDisplayLogsDetailsComponent;
  let fixture: ComponentFixture<InDisplayLogsDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InDisplayLogsDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InDisplayLogsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
