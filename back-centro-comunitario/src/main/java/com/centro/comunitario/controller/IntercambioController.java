package com.centro.comunitario.controller;

import com.centro.comunitario.DTO.IntercambioRequest;
import com.centro.comunitario.model.Intercambio;
import com.centro.comunitario.services.IntercambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intercambios")
public class IntercambioController {

    @Autowired
    private IntercambioService intercambioService;

    /**
     * Endpoint para realizar um intercâmbio entre dois centros.
     *
     * Exemplo JSON do corpo:
     * {
     *   "origemId": "id-do-centro-1",
     *   "destinoId": "id-do-centro-2",
     *   "recursosEnviados": {
     *       "MEDICO": 1,
     *       "VOLUNTARIO": 2
     *   },
     *   "recursosRecebidos": {
     *       "VEICULO": 1,
     *       "CESTA_BASICA": 3
     *   }
     * }
     */
    @PostMapping
    public ResponseEntity<Intercambio> realizarIntercambio(@RequestBody IntercambioRequest request) {
        Intercambio realizado = intercambioService.realizarIntercambio(
                request.getOrigemId(),
                request.getDestinoId(),
                request.getRecursosEnviados(),
                request.getRecursosRecebidos());

        return ResponseEntity.status(201).body(realizado);
    }
}
