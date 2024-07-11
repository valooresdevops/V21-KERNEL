import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InDisplayLogsComponent } from './in-display-logs.component';

describe('InDisplayLogsComponent', () => {
  let component: InDisplayLogsComponent;
  let fixture: ComponentFixture<InDisplayLogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InDisplayLogsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InDisplayLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
