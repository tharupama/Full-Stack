import { TestBed } from '@angular/core/testing';

import { AuthGuaed } from './auth-guaed';

describe('AuthGuaed', () => {
  let service: AuthGuaed;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthGuaed);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
