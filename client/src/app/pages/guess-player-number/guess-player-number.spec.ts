import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuessPlayerNumber } from './guess-player-number';

describe('GuessPlayerNumber', () => {
  let component: GuessPlayerNumber;
  let fixture: ComponentFixture<GuessPlayerNumber>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuessPlayerNumber],
    }).compileComponents();

    fixture = TestBed.createComponent(GuessPlayerNumber);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
