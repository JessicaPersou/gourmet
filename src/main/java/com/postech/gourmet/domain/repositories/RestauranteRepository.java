package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {

    Restaurante save(Restaurante restaurante);

    Optional<Restaurante> findById(Long id);

    boolean existsById(Long id);

    List<Restaurante> findAll();

    void deleteById(Long id);

    List<Restaurante> findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
            String nome, String endereco, String tipoCozinha);

    List<Restaurante> findByNomeContaining(String nome);

    boolean existsByNomeAndEndereco(String nome, String endereco);
}