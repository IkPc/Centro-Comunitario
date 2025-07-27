import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { CentroComunitario } from '../../core/models/centro.model';
import { RECURSO_PONTUACAO, RecursoTipo } from '../../core/models/recurso-tipo.model';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-intercambio-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './intercambio-form.component.html'
})
export class IntercambioFormComponent implements OnInit {
  title = 'Intercâmbio de Recursos';
  centros: CentroComunitario[] = [];
  form: FormGroup;

  recursoTipos = Object.values(RecursoTipo).filter(value => typeof value === 'string') as string[];

  RECURSO_TIPO_LABELS: Record<string, string> = {
    MEDICO: 'Médico',
    VOLUNTARIO: 'Voluntário',
    KIT_MEDICO: 'Kit Médico',
    VEICULO: 'Veículo',
    CESTA_BASICA: 'Cesta Básica'
  };

  mensagemErro: string | null = null;
  mensagemSucesso: string | null = null;

  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.form = this.fb.group({
      origemId: ['', Validators.required],
      destinoId: ['', Validators.required],
      recursosEnviados: this.fb.group({}),
      recursosRecebidos: this.fb.group({})
    });
  }

  ngOnInit(): void {
    this.http.get<CentroComunitario[]>(`${environment.apiBaseUrl}/centros`)
      .subscribe(centros => {
        this.centros = centros;

        this.recursoTipos.forEach(tipo => {
          (this.form.get('recursosEnviados') as FormGroup).addControl(tipo, this.fb.control(0, [Validators.min(0)]));
          (this.form.get('recursosRecebidos') as FormGroup).addControl(tipo, this.fb.control(0, [Validators.min(0)]));
        });
      });
  }

  calcularPontos(recursos: {[key: string]: number}): number {
    return Object.entries(recursos)
      .reduce((acc, [tipo, qtd]) => acc + (RECURSO_PONTUACAO[tipo as RecursoTipo] * qtd), 0);
  }

  podeEnviar(): boolean {
    if (this.form.invalid) return false;
    if (this.form.value.origemId === this.form.value.destinoId) return false;

    const pontosEnviados = this.calcularPontos(this.form.value.recursosEnviados);
    const pontosRecebidos = this.calcularPontos(this.form.value.recursosRecebidos);

    const origem = this.centros.find(c => c.id === this.form.value.origemId);
    const destino = this.centros.find(c => c.id === this.form.value.destinoId);

    if (!origem || !destino) return false;

    const origemCritico = (origem.ocupacaoAtual / origem.capacidadeMaxima) > 0.9;
    const destinoCritico = (destino.ocupacaoAtual / destino.capacidadeMaxima) > 0.9;

    if (!origemCritico && !destinoCritico) {
      return pontosEnviados === pontosRecebidos && pontosEnviados > 0;
    } else if (origemCritico) {
      return pontosRecebidos >= pontosEnviados && pontosEnviados > 0;
    } else if (destinoCritico) {
      return pontosEnviados >= pontosRecebidos && pontosRecebidos > 0;
    }
    return false;
  }

  enviar() {
    if (!this.podeEnviar()) {
      this.mensagemErro = 'Validação falhou: verifique os dados e as regras de pontos.';
      this.mensagemSucesso = null;
      return;
    }
    this.mensagemErro = null;

    const payload = {
      origemId: this.form.value.origemId,
      destinoId: this.form.value.destinoId,
      recursosEnviados: this.form.value.recursosEnviados,
      recursosRecebidos: this.form.value.recursosRecebidos,
    };

    this.http.post(`${environment.apiBaseUrl}/intercambios`, payload)
      .subscribe({
        next: () => {
          this.mensagemSucesso = 'Intercâmbio realizado com sucesso!';
          this.form.reset();
        },
        error: (e) => {
          this.mensagemErro = 'Erro ao realizar intercâmbio: ' + (e.error?.message || e.statusText);
          this.mensagemSucesso = null;
        }
      });
  }
}
