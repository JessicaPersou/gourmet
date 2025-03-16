package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Restaurante;
import com.postech.gourmet.domain.repositories.RestauranteRepository;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.jpa.JpaRestauranteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepository {
    private final JpaRestauranteRepository jpaRestauranteRepository;

    public RestauranteRepositoryImpl(JpaRestauranteRepository jpaRestauranteRepository) {
        this.jpaRestauranteRepository = jpaRestauranteRepository;
    }

    @Override
    public Restaurante save(Restaurante restaurante) {
        RestauranteData data = RestauranteData.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .endereco(restaurante.getEndereco())
                .telefone(restaurante.getTelefone())
                .build();
        return jpaRestauranteRepository.save(data).toDomain();
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return jpaRestauranteRepository.findById(id).map(RestauranteData::toDomain);
    }

    @Override
    public List<Restaurante> findByNomeContaining(String nome) {
        return jpaRestauranteRepository.findByNomeContaining(nome).stream()
                .map(RestauranteData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRestauranteRepository.existsById(id);
    }
}