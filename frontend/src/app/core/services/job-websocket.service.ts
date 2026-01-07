import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class JobWebsocketService {

  private client!: Client;

  private readonly newJobSubject = new BehaviorSubject<boolean>(false);
  newJob$ = this.newJobSubject.asObservable();

  connect(): void {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      debug: () => {}
    });

    this.client.onConnect = () => {
      this.client.subscribe('/topic/jobs', () => {
        this.newJobSubject.next(true);
      });
    };

    this.client.activate();
  }

  clearNewJobIndicator(): void {
    this.newJobSubject.next(false);
  }
}
