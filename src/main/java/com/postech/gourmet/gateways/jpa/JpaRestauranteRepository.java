package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.RestauranteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaRestauranteRepository extends JpaRepository<RestauranteData, Long> {
    /**
     * Busca restaurantes por nome
     */
    List<RestauranteData> findByNomeContaining(String nome);

    /**
     * Busca restaurantes por nome, endereço ou tipo de cozinha
     */
    @Query("SELECT r FROM RestauranteData r " +
            "WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(r.endereco) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(r.tipoCozinha) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<RestauranteData> findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
            @Param("termo") String termo);

    /**
     * Verifica se já existe um restaurante com o mesmo nome e endereço
     */
    boolean existsByNomeAndEndereco(String nome, String endereco);
}