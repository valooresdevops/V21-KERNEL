import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AmlComponent } from './aml.component';

describe('AmlComponent', () => {
  let component: AmlComponent;
  let fixture: ComponentFixture<AmlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AmlComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AmlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
