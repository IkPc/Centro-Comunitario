import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Intercambio } from '../models/intercambio.model';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class IntercambioService {
  private baseUrl = `${environment.apiBaseUrl}/intercambios`;

  constructor(private http: HttpClient) {}

  criar(intercambio: Intercambio): Observable<Intercambio> {
    return this.http.post<Intercambio>(this.baseUrl, intercambio);
  }
}
