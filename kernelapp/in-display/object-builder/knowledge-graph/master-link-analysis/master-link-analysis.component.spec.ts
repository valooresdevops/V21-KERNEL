
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterLinkAnalysisComponent } from './master-link-analysis.component';

describe('MasterLinkAnalysisComponent', () => {
  let component: MasterLinkAnalysisComponent;
  let fixture: ComponentFixture<MasterLinkAnalysisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MasterLinkAnalysisComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MasterLinkAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});