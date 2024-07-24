import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KwgCytoscapeComponent } from './kwg-cytoscape.component';

describe('KwgCytoscapeComponent', () => {
  let component: KwgCytoscapeComponent;
  let fixture: ComponentFixture<KwgCytoscapeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KwgCytoscapeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(KwgCytoscapeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
