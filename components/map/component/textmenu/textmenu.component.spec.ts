import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TextmenuComponent } from './textmenu.component';

describe('TextmenuComponent', () => {
  let component: TextmenuComponent;
  let fixture: ComponentFixture<TextmenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TextmenuComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TextmenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
