import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardAccessRightsComponent } from './dashboard-access-rights.component';

describe('DashboardAccessRightsComponent', () => {
  let component: DashboardAccessRightsComponent;
  let fixture: ComponentFixture<DashboardAccessRightsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardAccessRightsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashboardAccessRightsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
