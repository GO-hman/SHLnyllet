import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { GuessPlayerViewInput, GuessPlayerViewOutput, ShlControllerService, ShlPlayer } from '../api';
import { firstValueFrom } from 'rxjs';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ReactiveFormsModule, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
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

  async fetchPlayer(){
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

  async ngOnInit(){
    await this.fetchPlayer();
  }

  async onSubmit(){
    if(this.form.invalid){
      alert("Invalid form");
      return;
    }
    const player = this.currPlayer();
    if(!player?.uuid){
      return;
    }
    try {
      const response = await firstValueFrom(this.shlService.guessPlayer({
        id: player.uuid,
        name: this.form.value.name!,
      }, 'response'));

      if (response.status === 200) {
        this.guessCorrect.set(true);
        this.fetchPlayer();
      } else {
        this.guessCorrect.set(false);
      }
    } catch (err) {
      this.error.set(err instanceof HttpErrorResponse ? err.message : 'Failed to submit guess');
    }
  }
}
