import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CentroService } from '../../core/services/centro.service';
import { CentroComunitario } from '../../core/models/centro.model';

@Component({
  selector: 'app-lista-centros',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-centros.component.html',
  styleUrl: './lista-centros.component.scss',
})
export class ListaCentrosComponent implements OnInit {
  title = 'Lista de Centros Comunitários';
  centros: CentroComunitario[] = [];

  constructor(private centroService: CentroService) {}

  ngOnInit(): void {
  this.centroService.listar().subscribe({
    next: (res) => {
      console.log('Dados recebidos:', res);
      this.centros = res;
    },
    error: (err) => console.error('Erro ao buscar centros:', err),
  });
}

}
