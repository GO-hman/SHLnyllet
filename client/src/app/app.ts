import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { GuessPlayerName } from './pages/guess-player-name/guess-player-name';
import { MatTabGroup, MatTabsModule } from '@angular/material/tabs';
import { GuessPlayerNumber } from './pages/guess-player-number/guess-player-number';

@Component({
  selector: 'app-root',
  imports: [
    // RouterOutlet,
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    GuessPlayerName,
    MatTabsModule,
    GuessPlayerNumber,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('client');
}
