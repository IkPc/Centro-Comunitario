package com.centro.comunitario.repository;

import com.centro.comunitario.model.Intercambio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IntercambioRepository extends MongoRepository<Intercambio, String> {
}
