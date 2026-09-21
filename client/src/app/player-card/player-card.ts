import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, Input, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { GameType, GuessPlayerViewOutput, ShlControllerService, ShlTeam } from '../../api';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-player-card',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    MatProgressSpinner,
    MatSelectModule,
    MatAutocompleteModule,
  ],
  templateUrl: './player-card.html',
  styleUrl: './player-card.css',
})
export class PlayerCard {
  shlService = inject(ShlControllerService);

  // @Input() game:  = 'GUESS_NAME';
  @Input() game: GameType = GameType.GUESS_NAME;

  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  imageLoaded = signal<boolean>(false);
  currPlayer = signal<GuessPlayerViewOutput | null>(null);

  selectedTeam = signal<ShlTeam | null>(null);
  allTeams = signal<ShlTeam[] | null>(null);
  //Player logic

  onImageLoad() {
    this.imageLoaded.set(true);
  }

  async fetchPlayer() {
    const team = this.selectedTeam();
    team === null
      ? await this.fetchRandomPlayer()
      : await this.fetchRandomPlayerForTeam(team.uuid!);
  }

  async fetchRandomPlayer() {
    this.loading.set(true);
    this.error.set(null);
    this.imageLoaded.set(false);
    try {
      var randomPlayer = await firstValueFrom(this.shlService.randomPlayer(this.game));
      this.currPlayer.set(randomPlayer);
    } catch (err) {
      this.currPlayer.set(null);
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to fetch player');
    }
    this.loading.set(false);
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

  async onNewPlayer() {
    await this.fetchPlayer();
  }

  //Team logic
  onTeamChange(team: ShlTeam | undefined) {
    if (!team?.uuid) {
      this.fetchRandomPlayer();
      return;
    }
    this.fetchRandomPlayerForTeam(team.uuid);
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

  async ngOnInit() {
    await this.fetchPlayer();
    await this.fetchTeams();
  }
}
