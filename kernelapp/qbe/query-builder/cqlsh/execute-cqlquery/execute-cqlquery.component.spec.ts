import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecuteCqlQueryComponent } from './execute-cqlquery.component';

describe('ExecuteCqlQueryComponent', () => {
  let component: ExecuteCqlQueryComponent;
  let fixture: ComponentFixture<ExecuteCqlQueryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExecuteCqlQueryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecuteCqlQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
