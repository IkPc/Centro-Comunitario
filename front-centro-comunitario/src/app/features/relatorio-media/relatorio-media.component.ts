import { Component, OnInit } from '@angular/core';
import { RelatorioService } from '../../core/services/relatorio.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-relatorio-media',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './relatorio-media.component.html',
})
export class RelatorioMediaComponent implements OnInit {
  title = 'Relatório de Média de Recursos';
  media: { tipo: string, valor: number }[] = [];

  constructor(private relatorioService: RelatorioService) {}

  ngOnInit() {
    this.relatorioService.getMediaRecursos().subscribe((res) => {
      this.media = Object.entries(res).map(([tipo, valor]) => ({ tipo, valor }));
    });
  }
}
