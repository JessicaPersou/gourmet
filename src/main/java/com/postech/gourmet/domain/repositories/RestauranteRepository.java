package com.postech.gourmet.domain.repositories;

import com.postech.gourmet.domain.entities.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {
    /**
     * Salva um restaurante
     */
    Restaurante save(Restaurante restaurante);

    /**
     * Busca um restaurante pelo ID
     */
    Optional<Restaurante> findById(Long id);

    /**
     * Verifica se um restaurante existe pelo ID
     */
    boolean existsById(Long id);

    /**
     * Lista todos os restaurantes
     */
    List<Restaurante> findAll();

    /**
     * Exclui um restaurante
     */
    void deleteById(Long id);

    /**
     * Busca restaurantes por nome, endereço ou tipo de cozinha
     */
    List<Restaurante> findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
            String nome, String endereco, String tipoCozinha);

    /**
     * Busca restaurantes por nome
     */
    List<Restaurante> findByNomeContaining(String nome);

    /**
     * Verifica se já existe um restaurante com o mesmo nome e endereço
     */
    boolean existsByNomeAndEndereco(String nome, String endereco);
}