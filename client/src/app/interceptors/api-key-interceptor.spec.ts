import { TestBed } from '@angular/core/testing';

import { ApiKeyInterceptor } from './api-key-interceptor';

describe('ApiKeyInterceptor', () => {
  let interceptor: ApiKeyInterceptor;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApiKeyInterceptor],
    });
    interceptor = TestBed.inject(ApiKeyInterceptor);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });
});
