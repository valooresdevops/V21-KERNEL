import { TestBed } from '@angular/core/testing';

import { OfflinedataService } from './offlinedata.service';

describe('OfflinedataService', () => {
  let service: OfflinedataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OfflinedataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
