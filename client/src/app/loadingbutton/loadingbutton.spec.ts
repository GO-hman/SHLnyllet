import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Loadingbutton } from './loadingbutton';

describe('Loadingbutton', () => {
  let component: Loadingbutton;
  let fixture: ComponentFixture<Loadingbutton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Loadingbutton],
    }).compileComponents();

    fixture = TestBed.createComponent(Loadingbutton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
