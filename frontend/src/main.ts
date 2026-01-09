declare global {
  interface Window {
    global: typeof globalThis;
  }
}

window.global = window;

/* istanbul ignore file */
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { register } from 'swiper/element/bundle'

register();

//testing

bootstrapApplication(App, appConfig).catch((err) => console.error(err));
