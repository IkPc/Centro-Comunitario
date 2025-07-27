package com.centro.comunitario.exceptions;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiError {
    private int status;
    private String erro;
    private String detalhe;
    private LocalDateTime timestamp;

    public ApiError(int status, String erro, String detalhe) {
        this.status = status;
        this.erro = erro;
        this.detalhe = detalhe;
        this.timestamp = LocalDateTime.now();
    }
}
