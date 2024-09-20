import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapOfflineComponent } from './map-offline.component';

describe('MapOfflineComponent', () => {
  let component: MapOfflineComponent;
  let fixture: ComponentFixture<MapOfflineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MapOfflineComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MapOfflineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
