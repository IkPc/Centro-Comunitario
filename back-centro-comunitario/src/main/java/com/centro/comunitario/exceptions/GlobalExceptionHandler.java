// package com.centro.comunitario.exceptions;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @ControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(OcupacaoInvalidaException.class)
//     public ResponseEntity<ApiError> handleOcupacaoInvalida(OcupacaoInvalidaException ex) {
//         ApiError erro = new ApiError(
//             HttpStatus.BAD_REQUEST.value(),
//             "Ocupação inválida",
//             ex.getMessage()
//         );
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
//     }

//     @ExceptionHandler(RecursoNaoEncontradoException.class)
//     public ResponseEntity<ApiError> handleNaoEncontrado(RecursoNaoEncontradoException ex) {
//         ApiError erro = new ApiError(
//             HttpStatus.NOT_FOUND.value(),
//             "Recurso não encontrado",
//             ex.getMessage()
//         );
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
//     }

//     @ExceptionHandler(Exception.class)
//     public ResponseEntity<ApiError> handleException(Exception ex) {
//         ApiError erro = new ApiError(
//             HttpStatus.INTERNAL_SERVER_ERROR.value(),
//             "Erro interno",
//             ex.getMessage()
//         );
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
//     }
// }
