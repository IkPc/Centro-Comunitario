package com.centro.comunitario.services;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.model.RecursoTipo;
import com.centro.comunitario.repository.CentroComunitarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CentroComunitarioServiceTest {

    private CentroComunitarioRepository repository;
    private CentroComunitarioService service;

    @BeforeEach
    void setUp() {
        repository = mock(CentroComunitarioRepository.class);
        service = new CentroComunitarioService(repository);
    }

    // Teste listarTodos - deve retornar lista do repositório
    @Test
    void testListarTodos_DeveRetornarLista() {
        List<CentroComunitario> centros = Arrays.asList(new CentroComunitario(), new CentroComunitario());
        when(repository.findAll()).thenReturn(centros);

        List<CentroComunitario> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // Teste buscarPorId - centro existe
    @Test
    void testBuscarPorId_CentroExiste_DeveRetornarCentro() {
        CentroComunitario centro = new CentroComunitario();
        centro.setId("id123");
        when(repository.findById("id123")).thenReturn(Optional.of(centro));

        CentroComunitario resultado = service.buscarPorId("id123");

        assertNotNull(resultado);
        assertEquals("id123", resultado.getId());
    }

    // Teste buscarPorId - centro não existe
    @Test
    void testBuscarPorId_CentroNaoExiste_DeveLancar404() {
        when(repository.findById("id123")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            service.buscarPorId("id123");
        });
        assertEquals(404, ex.getStatusCode().value());
    }

    // Teste criar - salvar novo centro
    @Test
    void testCriar_DeveSalvarCentro() {
        CentroComunitario centro = new CentroComunitario();
        centro.setNome("Novo Centro");

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CentroComunitario criado = service.criar(centro);

        assertEquals("Novo Centro", criado.getNome());
        verify(repository, times(1)).save(centro);
    }

    // Teste deletar - centro existe
    @Test
    void testDeletar_CentroExiste_DeveDeletar() {
        when(repository.existsById("id1")).thenReturn(true);
        doNothing().when(repository).deleteById("id1");

        assertDoesNotThrow(() -> service.deletar("id1"));

        verify(repository, times(1)).deleteById("id1");
    }

    // Teste deletar - centro não existe
    @Test
    void testDeletar_CentroNaoExiste_DeveLancar404() {
        when(repository.existsById("id1")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            service.deletar("id1");
        });
        assertEquals(404, ex.getStatusCode().value());

        verify(repository, never()).deleteById(any());
    }

    //teste para verificar se a ocupação é atualizada corretamente
    @Test
    void testAtualizarOcupacao_ValorValido_DeveAtualizar() {
        CentroComunitario centro = new CentroComunitario();
        centro.setId("abc");
        centro.setCapacidadeMaxima(100);
        centro.setOcupacaoAtual(50);

        when(repository.findById("abc")).thenReturn(Optional.of(centro));
        when(repository.save(any())).thenReturn(centro);

        CentroComunitario atualizado = service.atualizarOcupacao("abc", 60);

        assertEquals(60, atualizado.getOcupacaoAtual());
    }

    //teste para verificar se a ocupação não pode ser maior que a capacidade máxima
    @Test
    void testAtualizarOcupacao_OcupacaoMaiorQueCapacidade_DeveLancarExcecao() {
        CentroComunitario centro = new CentroComunitario();
        centro.setId("abc");
        centro.setCapacidadeMaxima(100);
        centro.setOcupacaoAtual(90);

        when(repository.findById("abc")).thenReturn(Optional.of(centro));

        assertThrows(com.centro.comunitario.exceptions.OcupacaoInvalidaException.class, () -> {
            service.atualizarOcupacao("abc", 110);
        });
    }
}
