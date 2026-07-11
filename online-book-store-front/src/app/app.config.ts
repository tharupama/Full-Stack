import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZonelessChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';
import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideClientHydration(withEventReplay()),
    provideZonelessChangeDetection(),
    provideAnimations(),
    provideToastr({
      timeOut: 4000, // How long the toast stays visible (in ms)
      positionClass: 'toast-top-right', // Location of the toast popup
      preventDuplicates: true, // Avoid showing identical stacked toasts
      progressBar: true, // Show visual countdown line
      closeButton: true, // Add an 'X' to manually close it
    }),
  ],
};
