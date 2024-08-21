import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BubbleButtonComponent } from './bubble-button.component';

describe('BubbleButtonComponent', () => {
  let component: BubbleButtonComponent;
  let fixture: ComponentFixture<BubbleButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BubbleButtonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BubbleButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
