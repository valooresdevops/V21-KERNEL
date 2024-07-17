
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterLinkFormComponent } from './master-link-form.component';

describe('MasterLinkFormComponent', () => {
  let component: MasterLinkFormComponent;
  let fixture: ComponentFixture<MasterLinkFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MasterLinkFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MasterLinkFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});