import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NextLayerFormComponent } from './next-layer-form.component';

describe('NextLayerFormComponent', () => {
  let component: NextLayerFormComponent;
  let fixture: ComponentFixture<NextLayerFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NextLayerFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NextLayerFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
