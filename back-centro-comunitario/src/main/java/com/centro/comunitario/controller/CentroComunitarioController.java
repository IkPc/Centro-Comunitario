package com.centro.comunitario.controller;

import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.services.CentroComunitarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centros")
public class CentroComunitarioController {

    @Autowired
    private CentroComunitarioService centroService;

    // Listar todos os centros
    @GetMapping
    public List<CentroComunitario> listarTodos() {
        return centroService.listarTodos();
    }

    // Buscar centro por id
    @GetMapping("/{id}")
    public ResponseEntity<CentroComunitario> buscarPorId(@PathVariable String id) {
        CentroComunitario centro = centroService.buscarPorId(id);
        return ResponseEntity.ok(centro);
    }

    // Criar novo centro
    @PostMapping
    public ResponseEntity<CentroComunitario> criar(@RequestBody CentroComunitario centro) {
        CentroComunitario criado = centroService.criar(centro);
        return ResponseEntity.status(201).body(criado);
    }

    // Atualizar dados do centro (exceto ocupação)
    @PutMapping("/{id}")
    public ResponseEntity<CentroComunitario> atualizar(@PathVariable String id, @RequestBody CentroComunitario dadosAtualizados) {
        CentroComunitario atualizado = centroService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(atualizado);
    }

    // Atualizar ocupação
    @PatchMapping("/{id}/ocupacao")
    public ResponseEntity<CentroComunitario> atualizarOcupacao(@PathVariable String id, @RequestParam int ocupacao) {
        CentroComunitario atualizado = centroService.atualizarOcupacao(id, ocupacao);
        return ResponseEntity.ok(atualizado);
    }

    // Deletar centro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        centroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
