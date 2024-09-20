import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvancedSearchConfigurationFormComponent } from './advanced-search-configuration-form.component';

describe('AdvancedSearchConfigurationFormComponent', () => {
  let component: AdvancedSearchConfigurationFormComponent;
  let fixture: ComponentFixture<AdvancedSearchConfigurationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdvancedSearchConfigurationFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdvancedSearchConfigurationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
