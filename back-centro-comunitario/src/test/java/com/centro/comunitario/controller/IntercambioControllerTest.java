package com.centro.comunitario.controller;

import com.centro.comunitario.DTO.IntercambioRequest;
import com.centro.comunitario.model.Intercambio;
import com.centro.comunitario.services.IntercambioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import com.centro.comunitario.model.RecursoTipo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IntercambioController.class)
class IntercambioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IntercambioService intercambioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void realizarIntercambio_DeveRetornar201ComIntercambio() throws Exception {
        IntercambioRequest request = new IntercambioRequest();
        request.setOrigemId("centro-1");
        request.setDestinoId("centro-2");
        request.setRecursosEnviados(Map.of(RecursoTipo.MEDICO, 1, RecursoTipo.VOLUNTARIO, 2));
        request.setRecursosRecebidos(Map.of(RecursoTipo.VEICULO, 1, RecursoTipo.CESTA_BASICA, 3));

        Intercambio mockResponse = new Intercambio();

        when(intercambioService.realizarIntercambio(
                request.getOrigemId(),
                request.getDestinoId(),
                request.getRecursosEnviados(),
                request.getRecursosRecebidos()))
            .thenReturn(mockResponse);

        mockMvc.perform(post("/api/intercambios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }
}
