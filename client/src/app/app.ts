import { Component, computed, inject, signal, viewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { PlayerNameViewOutput, ShlControllerService } from '../api';
import { firstValueFrom } from 'rxjs';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { toSignal } from '@angular/core/rxjs-interop';
import { Snackbar } from './snackbar/snackbar';
import { PlayerCard } from './player-card/player-card';

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
    MatSelectModule,
    MatAutocompleteModule,
    PlayerCard,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('client');

  playerCard = viewChild.required(PlayerCard);

  //DI
  shlService = inject(ShlControllerService);
  private fb = inject(FormBuilder);
  private _snackBar = inject(Snackbar);

  error = signal<string | null>(null);
  guessCorrect = signal<boolean | null>(null);
  correctCounter = signal<number>(0);
  playerNames = signal<PlayerNameViewOutput[]>([]);

  form = this.fb.group({
    name: ['', Validators.required],
  });

  nameValue = toSignal(this.form.controls.name.valueChanges, { initialValue: '' });
  filteredPlayers = computed(() => {
    const term = (this.nameValue() ?? '').toLowerCase().trim();
    if (!term) return [];
    return (this.playerNames() ?? [])
      .filter((p) => p.name?.toLowerCase().includes(term))
      .slice(0, 20);
  });

  async onNewPlayer() {
    await this.playerCard().fetchPlayer();
  }

  async ngOnInit() {
    this.playerNames.set((await firstValueFrom(this.shlService.playerNames())) ?? []);
  }

  async onSubmit() {
    this.guessCorrect.set(null);
    if (this.form.invalid) {
      alert('Invalid form');
      return;
    }
    const player = this.playerCard().currPlayer();
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
