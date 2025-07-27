package com.centro.comunitario.controller;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.services.CentroComunitarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CentroComunitarioControllerTest {

    @Mock
    private CentroComunitarioService centroService;

    @InjectMocks
    private CentroComunitarioController controller;

    private CentroComunitario centro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        centro = new CentroComunitario();
        centro.setId("123");
        centro.setNome("Centro A");
    }

    @Test
    void testListarTodosCentrosComunitarios() {
        List<CentroComunitario> lista = List.of(centro);
        when(centroService.listarTodos()).thenReturn(lista);

        List<CentroComunitario> resultado = controller.listarTodos();
        assertEquals(1, resultado.size());
        assertEquals("Centro A", resultado.get(0).getNome());
    }

    @Test
    void testBuscarPorId() {
        when(centroService.buscarPorId("123")).thenReturn(centro);

        ResponseEntity<CentroComunitario> response = controller.buscarPorId("123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Centro A", response.getBody().getNome());
    }

    @Test
    void testCriarCentroComunitario() {
        when(centroService.criar(any())).thenReturn(centro);

        ResponseEntity<CentroComunitario> response = controller.criar(centro);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Centro A", response.getBody().getNome());
    }

    @Test
    void testAtualizarCentroComunitario() {
        centro.setEndereco("Rua X");
        when(centroService.atualizar(eq("123"), any())).thenReturn(centro);

        ResponseEntity<CentroComunitario> response = controller.atualizar("123", centro);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Rua X", response.getBody().getEndereco());
    }

    @Test
    void testAtualizarOcupacao() {
        centro.setOcupacaoAtual(60);
        when(centroService.atualizarOcupacao("123", 60)).thenReturn(centro);

        ResponseEntity<CentroComunitario> response = controller.atualizarOcupacao("123", 60);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(60, response.getBody().getOcupacaoAtual());
    }

    @Test
    void testDeletarCentroComunitario() {
        doNothing().when(centroService).deletar("123");

        ResponseEntity<Void> response = controller.deletar("123");

        assertEquals(204, response.getStatusCode().value());
        verify(centroService, times(1)).deletar("123");
    }
}
