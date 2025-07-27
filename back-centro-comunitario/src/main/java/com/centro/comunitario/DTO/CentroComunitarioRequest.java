package com.centro.comunitario.DTO;

import java.util.List;

import com.centro.comunitario.model.Coordenadas;
import com.centro.comunitario.model.Recurso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentroComunitarioRequest {
    private String nome;
    private String endereco;
    private String cidade;
    private String estado;
    private Coordenadas coordenadas;
    private List<Recurso> recursosDisponiveis;
    private List<String> servicosOferecidos;
    private String status;
}
