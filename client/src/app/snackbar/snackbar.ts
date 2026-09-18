import { Component, inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class Snackbar {
  private _snackBar = inject(MatSnackBar);

  openSnackBar(message: string, action: string, duration?: number) {
    this._snackBar.open(message, action, { duration: duration ? duration : 2000 });
  }
}
