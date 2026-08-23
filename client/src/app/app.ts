import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import {
  GuessPlayerViewInput,
  GuessPlayerViewOutput,
  ShlControllerService,
  ShlPlayer,
} from '../api';
import { firstValueFrom } from 'rxjs';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    MatSlideToggle,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('client');

  //DI
  shlService = inject(ShlControllerService);
  private fb = inject(FormBuilder);

  currPlayer = signal<GuessPlayerViewOutput | null>(null);
  error = signal<string | null>(null);
  loading = signal<boolean>(false);
  guessCorrect = signal<boolean | null>(null);

  form = this.fb.group({
    name: ['', Validators.required],
  });

  async fetchPlayer() {
    this.loading.set(true);
    this.error.set(null);
    try {
      var randomPlayer = await firstValueFrom(this.shlService.randomPlayer());
      console.log(randomPlayer);
      this.currPlayer.set(randomPlayer);
    } catch (err) {
      this.currPlayer.set(null);
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to fetch player');
    }
    this.loading.set(false);
  }

  private _snackBar = inject(MatSnackBar);

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action);
  }

  async ngOnInit() {
    await this.fetchPlayer();
  }

  async onSubmit() {
    this.guessCorrect.set(null);
    if (this.form.invalid) {
      alert('Invalid form');
      return;
    }
    const player = this.currPlayer();
    if (!player?.uuid) {
      return;
    }
    try {
      const response = await firstValueFrom(
        this.shlService.guessPlayer(
          {
            id: player.uuid,
            name: this.form.value.name!,
          },
          'response',
        ),
      );

      if (response.status === 200) {
        this.guessCorrect.set(true);
        this.fetchPlayer();
        this.openSnackBar('Rätt', 'x');
      } else {
        this.guessCorrect.set(false);
        this.openSnackBar('Fel', 'x');
      }
    } catch (err) {
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to submit guess');
    }
  }
}
