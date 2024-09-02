import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectSizeManagerPopupComponent } from './object-size-manager-popup.component';

describe('ObjectSizeManagerPopupComponent', () => {
  let component: ObjectSizeManagerPopupComponent;
  let fixture: ComponentFixture<ObjectSizeManagerPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectSizeManagerPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ObjectSizeManagerPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
