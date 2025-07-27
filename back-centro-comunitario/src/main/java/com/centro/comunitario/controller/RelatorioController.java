package com.centro.comunitario.controller;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.model.Intercambio;
import com.centro.comunitario.model.RecursoTipo;
import com.centro.comunitario.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    // 1. Relatório de centros com ocupação crítica
    @GetMapping("/ocupacao-critica")
    public List<CentroComunitario> centrosComOcupacaoCritica() {
        return relatorioService.centrosCriticos();
    }

    // 2. Relatório de média de recursos por centro
    @GetMapping("/media-recursos")
    public Map<RecursoTipo, Double> mediaRecursosPorCentro() {
        return relatorioService.mediaRecursosPorCentro();
    }

    // 3. Histórico de negociações filtrado
    @GetMapping("/historico-intercambios")
    public List<Intercambio> historicoIntercambios(
            @RequestParam String centroId,
            @RequestParam("de") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio
    ) {
        return relatorioService.historicoPorCentroEPeriodo(centroId, dataInicio);
    }
}