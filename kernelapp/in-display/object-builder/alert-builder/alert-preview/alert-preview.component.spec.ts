import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertPreviewComponent } from './alert-preview.component';

describe('AlertPreviewComponent', () => {
  let component: AlertPreviewComponent;
  let fixture: ComponentFixture<AlertPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertPreviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AlertPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
