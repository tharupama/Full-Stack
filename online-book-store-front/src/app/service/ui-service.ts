import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root', // Makes it available app-wide
})
export class UiService {
  // Create a stream that components can listen to
  private toggleCartSubject = new Subject<void>();
  toggleCart$ = this.toggleCartSubject.asObservable();

  // Method to trigger the event
  openCart() {
    console.log('UiService: openCart called, emitting event...');
    this.toggleCartSubject.next();
  }
}
