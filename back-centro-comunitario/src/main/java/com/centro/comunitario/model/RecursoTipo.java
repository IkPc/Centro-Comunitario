package com.centro.comunitario.model;

public enum RecursoTipo {
    MEDICO(4),
    VOLUNTARIO(3),
    KIT_MEDICO(7),
    VEICULO(5),
    CESTA_BASICA(2);

    private final int pontos;

    RecursoTipo(int pontos) {
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }
}
