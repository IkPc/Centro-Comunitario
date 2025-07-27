package com.centro.comunitario.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recurso {
    private String tipo;
    private int quantidade;
    private String unidade;
}
