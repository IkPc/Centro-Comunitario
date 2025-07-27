package com.centro.comunitario.DTO;

import com.centro.comunitario.model.RecursoTipo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String origemId;

    @NotNull
    private String destinoId;

    @NotEmpty
    private Map<RecursoTipo, Integer> recursosEnviados;
    
    @NotEmpty
    private Map<RecursoTipo, Integer> recursosRecebidos;
}