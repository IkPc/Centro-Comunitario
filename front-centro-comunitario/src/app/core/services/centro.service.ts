import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CentroComunitario } from '../models/centro.model';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CentroService {
  private baseUrl = `${environment.apiBaseUrl}/centros`;

  constructor(private http: HttpClient) {}

  listar(): Observable<CentroComunitario[]> {
    return this.http.get<CentroComunitario[]>(this.baseUrl);
  }

  criar(centro: CentroComunitario): Observable<CentroComunitario> {
    return this.http.post<CentroComunitario>(this.baseUrl, centro);
  }

  atualizar(id: string, centro: Partial<CentroComunitario>): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}`, centro);
  }

  atualizarOcupacao(id: string, ocupacao: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/ocupacao`, { ocupacao });
  }
}
