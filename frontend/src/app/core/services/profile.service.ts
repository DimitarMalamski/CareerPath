import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProfileDto } from '../models/profile.dto';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = `${environment.apiUrl}/profile`;

  getMyProfile(): Observable<ProfileDto> {
    return this.http.get<ProfileDto>(`${this.apiUrl}/me`);
  }

  updateMyProfile(dto: ProfileDto): Observable<ProfileDto> {
    return this.http.put<ProfileDto>(`${this.apiUrl}/me`, dto);
  }
}
