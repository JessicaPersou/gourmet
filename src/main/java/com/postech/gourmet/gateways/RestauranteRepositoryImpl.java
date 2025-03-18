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
        RestauranteData data = convertToData(restaurante);
        return jpaRestauranteRepository.save(data).toDomain();
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return jpaRestauranteRepository.findById(id).map(RestauranteData::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRestauranteRepository.existsById(id);
    }

    @Override
    public List<Restaurante> findAll() {
        return jpaRestauranteRepository.findAll()
                .stream()
                .map(RestauranteData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRestauranteRepository.deleteById(id);
    }

    @Override
    public List<Restaurante> findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(
            String nome, String endereco, String tipoCozinha) {
        // Usamos o mesmo termo para os três campos
        return jpaRestauranteRepository.findByNomeContainingOrEnderecoContainingOrTipoCozinhaContaining(nome)
                .stream()
                .map(RestauranteData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurante> findByNomeContaining(String nome) {
        return jpaRestauranteRepository.findByNomeContaining(nome)
                .stream()
                .map(RestauranteData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNomeAndEndereco(String nome, String endereco) {
        return jpaRestauranteRepository.existsByNomeAndEndereco(nome, endereco);
    }

    private RestauranteData convertToData(Restaurante restaurante) {
        RestauranteData data = new RestauranteData();
        data.setId(restaurante.getId());
        data.setNome(restaurante.getNome());
        data.setEndereco(restaurante.getEndereco());
        data.setTelefone(restaurante.getTelefone());
        data.setTipoCozinha(restaurante.getTipoCozinha());

        // Não convertemos mesas e avaliações aqui para evitar referências circulares
        // Elas serão carregadas sob demanda pelos métodos específicos

        return data;
    }
}