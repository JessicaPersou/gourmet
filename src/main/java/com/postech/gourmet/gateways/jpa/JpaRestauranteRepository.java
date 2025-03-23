package com.postech.gourmet.gateways.jpa;

import com.postech.gourmet.gateways.data.RestauranteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaRestauranteRepository extends JpaRepository<RestauranteData, Long> {

    List<RestauranteData> findByNomeContaining(String nome);

    @Query("SELECT r FROM RestauranteData r " +
            "WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(r.endereco) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(r.tipoCozinha) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<RestauranteData> findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
            @Param("termo") String termo);

    boolean existsByNomeAndEndereco(String nome, String endereco);
}