package com.centro.comunitario.services;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.model.Intercambio;
import com.centro.comunitario.model.RecursoTipo;
import com.centro.comunitario.repository.CentroComunitarioRepository;
import com.centro.comunitario.repository.IntercambioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelatorioServiceTest {

    private CentroComunitarioRepository centroRepo;
    private IntercambioRepository intercambioRepo;
    private RelatorioService relatorioService;

    @BeforeEach
    void setup() {
        centroRepo = mock(CentroComunitarioRepository.class);
        intercambioRepo = mock(IntercambioRepository.class);
        relatorioService = new RelatorioService();
        relatorioService.centroRepository = centroRepo;
        relatorioService.intercambioRepository = intercambioRepo;
    }

    @Test
    void centrosCriticos_DeveRetornarApenasCentrosComOcupacaoCritica() {
        CentroComunitario c1 = mock(CentroComunitario.class);
        CentroComunitario c2 = mock(CentroComunitario.class);
        when(c1.isOcupacaoCritica()).thenReturn(true);
        when(c2.isOcupacaoCritica()).thenReturn(false);
        when(centroRepo.findAll()).thenReturn(List.of(c1, c2));

        List<CentroComunitario> resultado = relatorioService.centrosCriticos();
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(c1));
    }

    @Test
    void mediaRecursosPorCentro_DeveRetornarMediasCorretas() {
        CentroComunitario c1 = new CentroComunitario();
        CentroComunitario c2 = new CentroComunitario();
        c1.setRecursos(of(RecursoTipo.MEDICO, 2, RecursoTipo.VOLUNTARIO, 4));
        c2.setRecursos(of(RecursoTipo.MEDICO, 4, RecursoTipo.VOLUNTARIO, 2));

        when(centroRepo.findAll()).thenReturn(List.of(c1, c2));

        Map<RecursoTipo, Double> resultado = relatorioService.mediaRecursosPorCentro();

        assertEquals(RecursoTipo.values().length, resultado.size());
        assertEquals(3.0, resultado.get(RecursoTipo.MEDICO));
        assertEquals(3.0, resultado.get(RecursoTipo.VOLUNTARIO));
    }

    @Test
    void historicoPorCentroEPeriodo_ComCentroIdValido_DeveFiltrarIntercambios() {
        LocalDateTime inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        Intercambio i1 = new Intercambio();
        i1.setCentroOrigemId("c1");
        i1.setCentroDestinoId("c2");
        i1.setDataHora(LocalDateTime.of(2023, 2, 1, 0, 0));

        Intercambio i2 = new Intercambio();
        i2.setCentroOrigemId("c1");
        i2.setCentroDestinoId("c3");
        i2.setDataHora(LocalDateTime.of(2022, 1, 1, 0, 0));

        when(intercambioRepo.findAll()).thenReturn(List.of(i1, i2));

        List<Intercambio> resultado = relatorioService.historicoPorCentroEPeriodo("c1", inicio);
        assertEquals(1, resultado.size());
        assertEquals(i1, resultado.get(0));
    }

    @Test
    void historicoPorCentroEPeriodo_ComCentroIdVazio_DeveLancarExcecao() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> relatorioService.historicoPorCentroEPeriodo("", LocalDateTime.now()));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void mediaRecursosPorCentro_QuandoNaoHaCentros_DeveRetornarMapaVazio() {
        when(centroRepo.findAll()).thenReturn(List.of());
        Map<RecursoTipo, Double> resultado = relatorioService.mediaRecursosPorCentro();
        assertTrue(resultado.isEmpty());
    }
}