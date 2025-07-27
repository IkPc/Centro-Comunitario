package com.centro.comunitario.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Document(collection = "centros comunitarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentroComunitario {

    @Id
    private String id;

    private String nome;
    private String endereco;
    private String localizacao;
    private int capacidadeMaxima;
    private int ocupacaoAtual;

    private Map<RecursoTipo, Integer> recursos;

    public boolean isOcupacaoCritica() {
        return ((double) ocupacaoAtual / capacidadeMaxima) > 0.9;
    }
}