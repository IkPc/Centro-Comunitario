package com.centro.comunitario.services;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.model.Intercambio;
import com.centro.comunitario.model.RecursoTipo;
import com.centro.comunitario.repository.CentroComunitarioRepository;
import com.centro.comunitario.repository.IntercambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private CentroComunitarioRepository centroRepository;

    @Autowired
    private IntercambioRepository intercambioRepository;

    // 1. Retorna centros com ocupação acima de 90%
    public List<CentroComunitario> centrosCriticos() {
        return centroRepository.findAll().stream()
                .filter(CentroComunitario::isOcupacaoCritica)
                .collect(Collectors.toList());
    }

    // 2. Retorna média de recursos por tipo por centro
    public Map<RecursoTipo, Double> mediaRecursosPorCentro() {
        List<CentroComunitario> centros = centroRepository.findAll();

        Map<RecursoTipo, Integer> totalPorTipo = new HashMap<>();
        int totalCentros = centros.size();

        for (CentroComunitario centro : centros) {
            for (RecursoTipo tipo : RecursoTipo.values()) {
                int qtd = centro.getRecursos().getOrDefault(tipo, 0);
                totalPorTipo.put(tipo, totalPorTipo.getOrDefault(tipo, 0) + qtd);
            }
        }

        Map<RecursoTipo, Double> mediaPorTipo = new HashMap<>();
        for (Map.Entry<RecursoTipo, Integer> entry : totalPorTipo.entrySet()) {
            mediaPorTipo.put(entry.getKey(), (double) entry.getValue() / totalCentros);
        }

        return mediaPorTipo;
    }

    // 3. Histórico de negociação filtrado
    public List<Intercambio> historicoPorCentroEPeriodo(String centroId, LocalDateTime dataInicio) {
        if (centroId == null || centroId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CentroId é obrigatório.");
        }

        return intercambioRepository.findAll().stream()
                .filter(i ->
                        (i.getCentroOrigemId().equals(centroId) || i.getCentroDestinoId().equals(centroId)) &&
                        !i.getDataHora().isBefore(dataInicio))
                .collect(Collectors.toList());
    }
}