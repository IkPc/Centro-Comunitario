package com.centro.comunitario.DTO;

import com.centro.comunitario.model.RecursoTipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntercambioRequest {
    private String origemId;
    private String destinoId;
    private Map<RecursoTipo, Integer> recursosEnviados;
    private Map<RecursoTipo, Integer> recursosRecebidos;
}