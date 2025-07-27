export enum RecursoTipo {
  MEDICO = 'MEDICO',
  VOLUNTARIO = 'VOLUNTARIO',
  KIT_MEDICO = 'KIT_MEDICO',
  VEICULO = 'VEICULO',
  CESTA_BASICA = 'CESTA_BASICA'
}

export const RECURSO_TIPO_LABELS = {
  MEDICO: 'Médicos',
  VOLUNTARIO: 'Voluntários',
  KIT_MEDICO: 'Kits Médicos',
  VEICULO: 'Veículos',
  CESTA_BASICA: 'Cestas Básicas',
};

export const RECURSO_PONTUACAO = {
  MEDICO: 4,
  VOLUNTARIO: 3,
  KIT_MEDICO: 7,
  VEICULO: 5,
  CESTA_BASICA: 2
};