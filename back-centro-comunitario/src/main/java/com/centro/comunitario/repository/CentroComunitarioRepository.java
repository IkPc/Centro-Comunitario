package com.centro.comunitario.repository;

import com.centro.comunitario.model.CentroComunitario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CentroComunitarioRepository extends MongoRepository<CentroComunitario, String> {
}
