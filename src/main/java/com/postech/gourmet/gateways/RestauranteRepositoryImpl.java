package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.gateways.jpa.JpaRestauranteRepository;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepository {
    private final JpaRestauranteRepository jpaRestauranteRepository;

    public RestauranteRepositoryImpl(JpaRestauranteRepository jpaRestauranteRepository) {
        this.jpaRestauranteRepository = jpaRestauranteRepository;
    }

    @Override
    public Restaurante save(Restaurante restaurante) {
        return jpaRestauranteRepository.save(restaurante);
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return jpaRestauranteRepository.findById(id);
    }

    @Override
    public List<Restaurante> findByNomeContaining(String nome) {
        return jpaRestauranteRepository.findByNomeContaining(nome);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRestauranteRepository.existsById(id);
    }
}