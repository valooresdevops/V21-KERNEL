import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IconsFormComponent } from './icons-form.component';

describe('IconsFormComponent', () => {
  let component: IconsFormComponent;
  let fixture: ComponentFixture<IconsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IconsFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(IconsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
