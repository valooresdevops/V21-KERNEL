import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvanceRowIdComponent } from './advance-row-id.component';

describe('AdvanceRowIdComponent', () => {
  let component: AdvanceRowIdComponent;
  let fixture: ComponentFixture<AdvanceRowIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdvanceRowIdComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdvanceRowIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
