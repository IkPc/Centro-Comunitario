package com.centro.comunitario.services;

import com.centro.comunitario.exceptions.OcupacaoInvalidaException;
import com.centro.comunitario.model.CentroComunitario;
import com.centro.comunitario.repository.CentroComunitarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CentroComunitarioService {

    @Autowired
    private CentroComunitarioRepository centroRepository;
    
    public CentroComunitarioService(CentroComunitarioRepository centroRepository) {
        this.centroRepository = centroRepository;
    }

    // Buscar todos os centros
    public List<CentroComunitario> listarTodos() {
        return centroRepository.findAll();
    }

    // Buscar centro por id
    public CentroComunitario buscarPorId(String id) {
        return centroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro não encontrado"));
    }

    // Criar novo centro
    public CentroComunitario criar(CentroComunitario centro) {
        // Aqui pode validar campos obrigatórios, por exemplo
        return centroRepository.save(centro);
    }

    // Atualizar dados do centro (exceto ocupação)
    public CentroComunitario atualizar(String id, CentroComunitario dadosAtualizados) {
        CentroComunitario centro = buscarPorId(id);

        centro.setNome(dadosAtualizados.getNome());
        centro.setEndereco(dadosAtualizados.getEndereco());
        centro.setLocalizacao(dadosAtualizados.getLocalizacao());
        centro.setCapacidadeMaxima(dadosAtualizados.getCapacidadeMaxima());
        centro.setRecursos(dadosAtualizados.getRecursos());

        return centroRepository.save(centro);
    }

    // Atualizar apenas ocupação atual
    public CentroComunitario atualizarOcupacao(String id, int novaOcupacao) {
        CentroComunitario centro = buscarPorId(id);

        if (novaOcupacao >= centro.getCapacidadeMaxima()) {
            throw new OcupacaoInvalidaException("Ocupação (" + novaOcupacao + ") não pode exceder capacidade máxima (" + centro.getCapacidadeMaxima() + ").");
        }
        centro.setOcupacaoAtual(novaOcupacao);

        // Aqui pode disparar notificação para outro microserviço
        if (centro.isOcupacaoCritica()) {
            System.out.println("⚠️ Centro " + centro.getNome() + " está com ocupação > 90%");
        }

        return centroRepository.save(centro);
    }

    // Deletar centro
    public void deletar(String id) {
        if (!centroRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro não encontrado");
        }
        centroRepository.deleteById(id);
    }
}
