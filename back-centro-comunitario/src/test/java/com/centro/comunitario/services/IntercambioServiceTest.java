package com.centro.comunitario.services;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.model.Intercambio;
import com.centro.comunitario.model.RecursoTipo;
import com.centro.comunitario.repository.CentroComunitarioRepository;
import com.centro.comunitario.repository.IntercambioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IntercambioServiceTest {

    private CentroComunitarioRepository centroRepository;
    private IntercambioRepository intercambioRepository;
    private IntercambioService service;

    @BeforeEach
    void setUp() {
        centroRepository = mock(CentroComunitarioRepository.class);
        intercambioRepository = mock(IntercambioRepository.class);
        service = new IntercambioService();
        service.centroRepository = centroRepository;
        service.intercambioRepository = intercambioRepository;
    }

    private CentroComunitario criarCentro(String id, String nome, Map<RecursoTipo, Integer> recursos, int capacidade, int ocupacao) {
        CentroComunitario c = new CentroComunitario();
        c.setId(id);
        c.setNome(nome);
        c.setRecursos(new HashMap<>(recursos));
        c.setCapacidadeMaxima(capacidade);
        c.setOcupacaoAtual(ocupacao);
        return c;
    }

    @Test
    void testIntercambioComSucesso() {
        Map<RecursoTipo, Integer> recursosOrigem = Map.of(RecursoTipo.MEDICO, 1);
        Map<RecursoTipo, Integer> recursosDestino = Map.of(RecursoTipo.CESTA_BASICA, 2);

        CentroComunitario origem = criarCentro("o1", "Origem", Map.of(RecursoTipo.MEDICO, 5, RecursoTipo.CESTA_BASICA, 5), 100, 50);
        CentroComunitario destino = criarCentro("d1", "Destino", Map.of(RecursoTipo.MEDICO, 3, RecursoTipo.CESTA_BASICA, 10), 100, 50);

        when(centroRepository.findById("o1")).thenReturn(Optional.of(origem));
        when(centroRepository.findById("d1")).thenReturn(Optional.of(destino));
        when(intercambioRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(centroRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Intercambio resultado = service.realizarIntercambio("o1", "d1", recursosOrigem, recursosDestino);

        // Verifica se recursos foram atualizados corretamente
        assertEquals(4, origem.getRecursos().get(RecursoTipo.MEDICO));
        assertEquals(7, origem.getRecursos().get(RecursoTipo.CESTA_BASICA));
        assertEquals(4, destino.getRecursos().get(RecursoTipo.MEDICO));
        assertEquals(8, destino.getRecursos().get(RecursoTipo.CESTA_BASICA));

        // Verifica dados do intercâmbio
        assertEquals("o1", resultado.getCentroOrigemId());
        assertEquals("d1", resultado.getCentroDestinoId());
        assertEquals(recursosOrigem, resultado.getRecursosEnviados());
        assertEquals(recursosDestino, resultado.getRecursosRecebidos());

        verify(centroRepository, times(2)).save(any());
        verify(intercambioRepository, times(1)).save(any());
    }

    @Test
    void testIntercambioMesmoCentroLancaException() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            service.realizarIntercambio("o1", "o1", Map.of(), Map.of());
        });
        assertEquals(400, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Centros devem ser diferentes"));
    }

    @Test
    void testIntercambioCentroOrigemNaoEncontrado() {
        when(centroRepository.findById("o1")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            service.realizarIntercambio("o1", "d1", Map.of(), Map.of());
        });

        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Centro origem não encontrado"));
    }

    @Test
    void testIntercambioCentroDestinoNaoEncontrado() {
        when(centroRepository.findById("o1")).thenReturn(Optional.of(new CentroComunitario()));
        when(centroRepository.findById("d1")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            service.realizarIntercambio("o1", "d1", Map.of(), Map.of());
        });

        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Centro destino não encontrado"));
    }

    @Test
    void testIntercambioRecursoInsuficiente() {
        CentroComunitario origem = criarCentro("o1", "Origem", Map.of(RecursoTipo.MEDICO, 1), 100, 50);
        CentroComunitario destino = criarCentro("d1", "Destino", Map.of(RecursoTipo.MEDICO, 10), 100, 50);

        when(centroRepository.findById("o1")).thenReturn(Optional.of(origem));
        when(centroRepository.findById("d1")).thenReturn(Optional.of(destino));

        Map<RecursoTipo, Integer> recursosOrigem = Map.of(RecursoTipo.MEDICO, 2); // mais do que tem

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            service.realizarIntercambio("o1", "d1", recursosOrigem, Map.of());
        });

        assertEquals(400, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("não possui recursos suficientes"));
    }

    @Test
    void testIntercambioPontosDiferentesSemOcupacaoCritica() {
        CentroComunitario origem = criarCentro("o1", "Origem", Map.of(RecursoTipo.MEDICO, 5), 100, 50);
        CentroComunitario destino = criarCentro("d1", "Destino", Map.of(RecursoTipo.VOLUNTARIO, 5), 100, 50);

        when(centroRepository.findById("o1")).thenReturn(Optional.of(origem));
        when(centroRepository.findById("d1")).thenReturn(Optional.of(destino));

        Map<RecursoTipo, Integer> recursosOrigem = Map.of(RecursoTipo.MEDICO, 1);   // 4 pontos
        Map<RecursoTipo, Integer> recursosDestino = Map.of(RecursoTipo.VOLUNTARIO, 1); // 3 pontos

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            service.realizarIntercambio("o1", "d1", recursosOrigem, recursosDestino);
        });

        assertEquals(400, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Pontos diferentes"));
    }

}
