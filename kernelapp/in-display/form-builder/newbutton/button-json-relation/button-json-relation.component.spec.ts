import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ButtonJsonRelationComponent } from './button-json-relation.component';

describe('ButtonJsonRelationComponent', () => {
  let component: ButtonJsonRelationComponent;
  let fixture: ComponentFixture<ButtonJsonRelationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ButtonJsonRelationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ButtonJsonRelationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
