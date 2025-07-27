package com.centro.comunitario.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentroComunitarioResponse {
    private String id;
    private String nome;
    private String endereco;
    private String cidade;
    private String estado;
    private List<String> servicosOferecidos;
    private String status;
}