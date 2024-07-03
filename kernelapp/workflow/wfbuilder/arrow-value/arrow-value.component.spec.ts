import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArrowValueComponent } from './arrow-value.component';

describe('ArrowValueComponent', () => {
  let component: ArrowValueComponent;
  let fixture: ComponentFixture<ArrowValueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArrowValueComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArrowValueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
