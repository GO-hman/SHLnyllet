import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuessPlayerName } from './guess-player-name';

describe('GuessPlayerName', () => {
  let component: GuessPlayerName;
  let fixture: ComponentFixture<GuessPlayerName>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuessPlayerName],
    }).compileComponents();

    fixture = TestBed.createComponent(GuessPlayerName);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
