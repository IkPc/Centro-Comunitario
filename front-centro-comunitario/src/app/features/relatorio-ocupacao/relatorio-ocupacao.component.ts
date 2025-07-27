import { Component, OnInit } from '@angular/core';
import { RelatorioService } from '../../core/services/relatorio.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-relatorio-ocupacao',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './relatorio-ocupacao.component.html',
})
export class RelatorioOcupacaoComponent implements OnInit {
  title = 'Relatório de Ocupação dos Centros Comunitários';
  centros: any[] = [];

  constructor(private relatorioService: RelatorioService) {}

  ngOnInit() {
    this.relatorioService.getCentrosCriticos().subscribe((dados) => {
      this.centros = dados;
    });
  }
}
