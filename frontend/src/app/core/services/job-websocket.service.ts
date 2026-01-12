import {Injectable, inject, PLATFORM_ID} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import type { Client } from '@stomp/stompjs';
import {JobsService} from '../../features/jobs/jobs.service';
import {Router} from '@angular/router';

@Injectable({ providedIn: 'root' })
export class JobWebsocketService {
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
  private readonly jobsService = inject(JobsService);
  private readonly router = inject(Router);

  private client: Client | null = null;
  private connected = false;

  private readonly newJobSubject = new BehaviorSubject<boolean>(false);
  readonly newJob$ = this.newJobSubject.asObservable();

  async connect(): Promise<void> {
    if (!this.isBrowser) return;

    if (this.connected) return;
    this.connected = true;

    const { Client } = await import('@stomp/stompjs');
    const SockJS = (await import('sockjs-client')).default;

    this.client = new Client({
      webSocketFactory: () => new SockJS('https://careerpath-ip.com/ws'),
      reconnectDelay: 5000,
      debug: (msg) => console.log('[STOMP]', msg)
    });

    this.client.onConnect = () => {
      console.log('[STOMP] connected');

      this.client?.subscribe('/topic/jobs', () => {
        console.log('[STOMP] new job event received');

        if (this.router.url.startsWith('/jobs')) {
          this.jobsService.reload();
        } else {
          this.newJobSubject.next(true);
        }
      });
    };

    this.client.onStompError = frame => {
      console.error('[STOMP] broker error', frame.headers['message']);
      console.error(frame.body);
    };

    this.client.activate();
  }

  clearNewJobIndicator(): void {
    this.newJobSubject.next(false);
  }
}
