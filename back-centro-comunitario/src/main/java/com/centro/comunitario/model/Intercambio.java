package com.centro.comunitario.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "intercambios")
public class Intercambio {

    @Id
    private String id;

    private String centroOrigemId;
    private String centroDestinoId;

    private Map<RecursoTipo, Integer> recursosEnviados;
    private Map<RecursoTipo, Integer> recursosRecebidos;

    private LocalDateTime dataHora;
}
