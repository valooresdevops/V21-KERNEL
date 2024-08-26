import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvancedSearchConfigurationComponent } from './advanced-search-configuration.component';

describe('AdvancedSearchConfigurationComponent', () => {
  let component: AdvancedSearchConfigurationComponent;
  let fixture: ComponentFixture<AdvancedSearchConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdvancedSearchConfigurationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdvancedSearchConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
