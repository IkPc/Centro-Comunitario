// relatorio.service.ts
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RelatorioService {
  private baseUrl = `${environment.apiBaseUrl}/relatorios`;

  constructor(private http: HttpClient) {}

  getCentrosCriticos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/ocupacao-critica`);
  }

  getMediaRecursos(): Observable<{ [tipo: string]: number }> {
    return this.http.get<{ [tipo: string]: number }>(`${this.baseUrl}/media-recursos`);
  }

  getHistoricoIntercambios(centroId: string, de: string): Observable<any[]> {
    const params = new HttpParams().set('centroId', centroId ?? '').set('de', de);
    return this.http.get<any[]>(`${this.baseUrl}/historico-intercambios`, { params });
  }
}
