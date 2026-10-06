import { Component, input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-loadingbutton',
  imports: [MatProgressSpinnerModule, MatButtonModule],
  templateUrl: './loadingbutton.html',
  styleUrl: './loadingbutton.css',
})
export class Loadingbutton {
  isLoading = input<boolean>(false);
}
