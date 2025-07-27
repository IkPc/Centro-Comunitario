package com.centro.comunitario.controller;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.model.Intercambio;
import com.centro.comunitario.model.RecursoTipo;
import com.centro.comunitario.services.RelatorioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RelatorioController.class)
class RelatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RelatorioService relatorioService;


    @Test
    void centrosComOcupacaoCritica_DeveRetornarLista() throws Exception {
        CentroComunitario mockCentro = new CentroComunitario();
        mockCentro.setId("c1");
        mockCentro.setNome("Centro Crítico");

        when(relatorioService.centrosCriticos()).thenReturn(List.of(mockCentro));

        mockMvc.perform(get("/api/relatorios/ocupacao-critica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("c1"))
                .andExpect(jsonPath("$[0].nome").value("Centro Crítico"));
    }

    @Test
    void mediaRecursosPorCentro_DeveRetornarMapeamento() throws Exception {
        when(relatorioService.mediaRecursosPorCentro()).thenReturn(Map.of(RecursoTipo.MEDICO, 2.5, RecursoTipo.VOLUNTARIO, 1.0));

        mockMvc.perform(get("/api/relatorios/media-recursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.MEDICO").value(2.5))
                .andExpect(jsonPath("$.VOLUNTARIO").value(1.0));
    }

    @Test
    void historicoIntercambios_DeveRetornarLista() throws Exception {
        Intercambio mockIntercambio = new Intercambio();
        mockIntercambio.setId("i1");

        String centroId = "centro-1";
        LocalDateTime inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        when(relatorioService.historicoPorCentroEPeriodo(centroId, inicio))
                .thenReturn(List.of(mockIntercambio));

        mockMvc.perform(get("/api/relatorios/historico-intercambios")
                        .param("centroId", centroId)
                        .param("de", inicio.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("i1"));
    }

    @Test
    void historicoIntercambios_ComParametrosInvalidos_DeveRetornar400() throws Exception {
        mockMvc.perform(get("/api/relatorios/historico-intercambios")
                        .param("centroId", "")
                        .param("de", "data-invalida"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void centrosComOcupacaoCritica_QuandoNaoHaCentros_DeveRetornarListaVazia() throws Exception {
        when(relatorioService.centrosCriticos()).thenReturn(List.of());

        mockMvc.perform(get("/api/relatorios/ocupacao-critica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}