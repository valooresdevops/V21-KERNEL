import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ElementBuilderPopupComponent } from './element-builder-popup.component';

describe('ElementBuilderPopupComponent', () => {
  let component: ElementBuilderPopupComponent;
  let fixture: ComponentFixture<ElementBuilderPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ElementBuilderPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ElementBuilderPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
