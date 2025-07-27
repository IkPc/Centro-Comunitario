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
import java.util.Map;

@Service
public class IntercambioService {

    @Autowired
    public CentroComunitarioRepository centroRepository;

    @Autowired
    public IntercambioRepository intercambioRepository;

    public Intercambio realizarIntercambio(
            String origemId,
            String destinoId,
            Map<RecursoTipo, Integer> recursosEnviados,
            Map<RecursoTipo, Integer> recursosRecebidos) {

        if (origemId.equals(destinoId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Centros devem ser diferentes");
        }

        CentroComunitario origem = centroRepository.findById(origemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro origem não encontrado"));

        CentroComunitario destino = centroRepository.findById(destinoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro destino não encontrado"));

        // Valida se os centros têm recursos suficientes para oferecer
        validarRecursosSuficientes(origem, recursosEnviados);
        validarRecursosSuficientes(destino, recursosRecebidos);

        // Calcula pontos oferecidos por cada centro
        int pontosOrigem = calcularPontos(recursosEnviados);
        int pontosDestino = calcularPontos(recursosRecebidos);

        // Se ocupação > 90% pode quebrar a regra de pontos
        boolean origemCritico = origem.isOcupacaoCritica();
        boolean destinoCritico = destino.isOcupacaoCritica();

        if (!origemCritico && !destinoCritico) {
            // Regra normal: pontos devem ser iguais
            if (pontosOrigem != pontosDestino) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Pontos diferentes: Origem oferece " + pontosOrigem + " e Destino " + pontosDestino);
            }
        } else {
            // Se um dos centros está crítico, o outro deve oferecer pelo menos os pontos do centro crítico
            if (origemCritico && pontosDestino < pontosOrigem) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Centro destino deve oferecer ao menos os pontos do centro origem crítico");
            }
            if (destinoCritico && pontosOrigem < pontosDestino) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Centro origem deve oferecer ao menos os pontos do centro destino crítico");
            }
        }

        // Atualiza recursos de origem e destino (subtrai e adiciona conforme o intercâmbio)
        transferirRecursos(origem, recursosEnviados, recursosRecebidos);
        transferirRecursos(destino, recursosRecebidos, recursosEnviados);

        // Salva centros atualizados
        centroRepository.save(origem);
        centroRepository.save(destino);

        // Cria histórico do intercâmbio
        Intercambio intercambio = new Intercambio();
        intercambio.setCentroOrigemId(origemId);
        intercambio.setCentroDestinoId(destinoId);
        intercambio.setRecursosEnviados(recursosEnviados);
        intercambio.setRecursosRecebidos(recursosRecebidos);
        intercambio.setDataHora(LocalDateTime.now());

        return intercambioRepository.save(intercambio);
    }

    // Verifica se o centro tem recursos suficientes para ofertar
    private void validarRecursosSuficientes(CentroComunitario centro, Map<RecursoTipo, Integer> recursosOfertados) {
        recursosOfertados.forEach((tipo, quantidade) -> {
            int disponivel = centro.getRecursos().getOrDefault(tipo, 0);
            if (quantidade <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Quantidade de recurso " + tipo + " deve ser positiva");
            }
            if (quantidade > disponivel) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Centro " + centro.getNome() + " não possui recursos suficientes de " + tipo);
            }
        });
    }

    // Calcula o total de pontos de um conjunto de recursos
    private int calcularPontos(Map<RecursoTipo, Integer> recursos) {
        return recursos.entrySet().stream()
                .mapToInt(e -> e.getKey().getPontos() * e.getValue())
                .sum();
    }

    // Transferência real dos recursos (subtrai os que oferece e adiciona os que recebe)
    private void transferirRecursos(CentroComunitario centro,
                                   Map<RecursoTipo, Integer> recursosOferecidos,
                                   Map<RecursoTipo, Integer> recursosRecebidos) {
        Map<RecursoTipo, Integer> atuais = centro.getRecursos();

        // Subtrai os recursos ofertados
        recursosOferecidos.forEach((tipo, quantidade) -> {
            int atual = atuais.getOrDefault(tipo, 0);
            atuais.put(tipo, atual - quantidade);
        });

        // Adiciona os recursos recebidos
        recursosRecebidos.forEach((tipo, quantidade) -> {
            int atual = atuais.getOrDefault(tipo, 0);
            atuais.put(tipo, atual + quantidade);
        });

        centro.setRecursos(atuais);
    }
}
