import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupNameShapeComponent } from './popup-name-shape.component';

describe('PopupNameShapeComponent', () => {
  let component: PopupNameShapeComponent;
  let fixture: ComponentFixture<PopupNameShapeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PopupNameShapeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PopupNameShapeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
