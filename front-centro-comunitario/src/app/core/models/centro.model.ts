export interface Recurso {
  medicos: number;
  voluntarios: number;
  kitsMedicos: number;
  veiculos: number;
  cestasBasicas: number;
}

export interface CentroComunitario {
  id?: string;
  nome: string;
  endereco: string;
  localizacao: {
    latitude: number;
    longitude: number;
  };
  capacidadeMaxima: number;
  ocupacaoAtual: number;
  recursos: Recurso;
}
