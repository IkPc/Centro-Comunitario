import { Component, OnInit } from '@angular/core';
import { RelatorioService } from '../../core/services/relatorio.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-relatorio-historico',
  standalone: true,
  imports: [CommonModule, FormsModule],
  providers: [DatePipe],
  templateUrl: './relatorio-historico.component.html',
})
export class RelatorioHistoricoComponent {
  title = 'Relatório de Histórico de Intercâmbios';
  centroId = '';
  dataInicio = '';
  historico: any[] = [];

  constructor(private relatorioService: RelatorioService) {}

    organizarRecursos() {
    this.historico.forEach((item) => {
      item.recursosEnviados = Object.entries(item.recursosEnviados)
        .map(([tipo, quantidade]) => `${tipo}: ${quantidade}`)
        .join(',\n');

      item.recursosRecebidos = Object.entries(item.recursosRecebidos)
        .map(([tipo, quantidade]) => `${tipo}: ${quantidade}`)
        .join(',\n');

      console.log(item.recursosEnviados, item.recursosRecebidos);
    });
  }


  buscar() {
    if (this.centroId && this.dataInicio) {
      this.relatorioService.getHistoricoIntercambios(this.centroId, this.dataInicio).subscribe((dados) => {
        this.historico = dados;
        this.organizarRecursos();
      });
    }
  }
}
