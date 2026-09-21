import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, signal, viewChild } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroupDirective,
  NgForm,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { ShlControllerService, GameType } from '../../../api';
import { PlayerCard } from '../../player-card/player-card';
import { Snackbar } from '../../snackbar/snackbar';
import { CommonModule } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-guess-player-number',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    MatSelectModule,
    MatAutocompleteModule,
    PlayerCard,
  ],
  templateUrl: './guess-player-number.html',
  styleUrl: './guess-player-number.css',
})
export class GuessPlayerNumber {
  playerCard = viewChild.required(PlayerCard);

  readonly GameType = GameType;

  //DI
  shlService = inject(ShlControllerService);
  private fb = inject(FormBuilder);
  private _snackBar = inject(Snackbar);

  error = signal<string | null>(null);
  guessCorrect = signal<boolean | null>(null);
  correctCounter = signal<number>(0);

  form = this.fb.group({
    number: [
      '',
      [Validators.required, Validators.pattern(/^\d+$/), Validators.min(1), Validators.max(99)],
    ],
  });

  async onSubmit() {
    this.guessCorrect.set(null);
    if (this.form.invalid) {
      return;
    }

    const player = this.playerCard().currPlayer();
    if (!player?.uuid) {
      return;
    }
    try {
      const number = Number(this.form.value.number!);

      const response = await firstValueFrom(
        this.shlService.guessNumber(
          {
            id: player.uuid,
            jerseyNumber: Number(this.form.value.number!),
          },
          'response',
        ),
      );

      if (response.status === 200) {
        this.guessCorrect.set(true);
        this.playerCard().fetchPlayer();
        this._snackBar.openSnackBar('Rätt', 'x');
        this.correctCounter.update((c) => c + 1);
        this.form.reset();
      } else {
        this.guessCorrect.set(false);
        this._snackBar.openSnackBar('Fel', 'x');
      }
    } catch (err) {
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to submit guess');
    }
  }
}
