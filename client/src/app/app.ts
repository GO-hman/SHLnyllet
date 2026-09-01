import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import {
  GuessPlayerViewOutput,
  ShlControllerService,
  ShlTeam,
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
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';

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
    MatProgressSpinner,
    MatSelectModule,
    // MatSlideToggle,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('client');

  //DI
  shlService = inject(ShlControllerService);
  private fb = inject(FormBuilder);
  private _snackBar = inject(MatSnackBar);

  currPlayer = signal<GuessPlayerViewOutput | null>(null);
  error = signal<string | null>(null);
  loading = signal<boolean>(false);
  guessCorrect = signal<boolean | null>(null);
  correctCounter = signal<number>(0);
  imageLoaded = signal<boolean>(false);
  allTeams = signal<ShlTeam[] | null>(null);
  selectedTeam = signal<ShlTeam | null>(null);

  form = this.fb.group({
    name: ['', Validators.required],
  });

  async fetchPlayer() {
    const team = this.selectedTeam();
    team === null
      ? await this.fetchRandomPlayer()
      : await this.fetchRandomPlayerForTeam(team.uuid!);
  }

  async fetchTeams() {
    this.loading.set(true);
    try {
      var teams = await firstValueFrom(this.shlService.allTeams());
      this.allTeams.set(teams);
    } catch (error) {
      this.allTeams.set(null);
      this.error.set(error instanceof HttpErrorResponse ? error.message : 'Failed To fetch teams');
    }
    this.loading.set(false);
  }

  async onNewPlayer() {
    await this.fetchPlayer();
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, { duration: 2000 });
  }

  onImageLoad() {
    this.imageLoaded.set(true);
  }

  async ngOnInit() {
    await this.fetchPlayer();
    await this.fetchTeams();
  }

  onTeamChange(team: ShlTeam | undefined) {
    if (!team?.uuid) {
      this.fetchRandomPlayer();
      return;
    }
    this.fetchRandomPlayerForTeam(team.uuid);
  }

  async fetchRandomPlayerForTeam(teamId: string) {
    this.loading.set(true);
    this.error.set(null);
    this.imageLoaded.set(false);
    try {
      const player = await firstValueFrom(this.shlService.randomPlayerFromTeam(teamId));
      this.currPlayer.set(player);
    } catch (err) {
      this.currPlayer.set(null);
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to fetch player');
    }
    this.loading.set(false);
  }

  async fetchRandomPlayer() {
    this.loading.set(true);
    this.error.set(null);
    this.imageLoaded.set(false);
    try {
      var randomPlayer = await firstValueFrom(this.shlService.randomPlayer());
      this.currPlayer.set(randomPlayer);
    } catch (err) {
      this.currPlayer.set(null);
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to fetch player');
    }
    this.loading.set(false);
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
        this.correctCounter.update((c) => c + 1);
        this.form.reset();
      } else {
        this.guessCorrect.set(false);
        this.openSnackBar('Fel', 'x');
      }
    } catch (err) {
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to submit guess');
    }
  }
}
