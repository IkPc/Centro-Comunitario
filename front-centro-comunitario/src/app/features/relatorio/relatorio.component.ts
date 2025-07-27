import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { RelatorioOcupacaoComponent } from '../relatorio-ocupacao/relatorio-ocupacao.component';
import { RelatorioMediaComponent } from '../relatorio-media/relatorio-media.component';
import { RelatorioHistoricoComponent } from '../relatorio-historico/relatorio-historico.component';

@Component({
  selector: 'app-relatorio',
  standalone: true,
  imports: [
    CommonModule,
    MatTabsModule,
    RelatorioOcupacaoComponent,
    RelatorioMediaComponent,
    RelatorioHistoricoComponent
  ],
  templateUrl: './relatorio.component.html',
  styleUrls: ['./relatorio.component.scss']
})
export class RelatorioComponent {
  title = 'Relatórios';
  activeTabIndex = 0;

  onTabChange(index: number) {
    this.activeTabIndex = index;
  }
}
