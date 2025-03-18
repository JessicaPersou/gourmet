package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Mesa;
import com.postech.gourmet.domain.repositories.MesaRepository;
import com.postech.gourmet.gateways.data.MesaData;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.jpa.JpaMesaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MesaRepositoryImpl implements MesaRepository {
    private final JpaMesaRepository jpaMesaRepository;

    public MesaRepositoryImpl(JpaMesaRepository jpaMesaRepository) {
        this.jpaMesaRepository = jpaMesaRepository;
    }

    @Override
    public Mesa save(Mesa mesa) {
        MesaData data = convertToData(mesa);
        return jpaMesaRepository.save(data).toDomain();
    }

    @Override
    public Optional<Mesa> findById(Long id) {
        return jpaMesaRepository.findById(id).map(MesaData::toDomain);
    }

    @Override
    public List<Mesa> findByRestauranteId(Long restauranteId) {
        return jpaMesaRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(MesaData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mesa> findDisponiveisByRestauranteIdAndDataHora(Long restauranteId, LocalDateTime dataHora) {
        // Consideramos um intervalo de 2 horas para cada reserva
        LocalDateTime fimReserva = dataHora.plusHours(2);

        return jpaMesaRepository.findDisponiveisByRestauranteIdAndDataHora(
                        restauranteId, dataHora, fimReserva)
                .stream()
                .map(MesaData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mesa> findDisponiveisByRestauranteIdAndDataHoraAndCapacidadeMinima(
            Long restauranteId, LocalDateTime dataHora, int capacidadeMinima) {
        // Consideramos um intervalo de 2 horas para cada reserva
        LocalDateTime fimReserva = dataHora.plusHours(2);

        return jpaMesaRepository.findDisponiveisByRestauranteIdAndDataHoraAndCapacidadeMinima(
                        restauranteId, dataHora, fimReserva, capacidadeMinima)
                .stream()
                .map(MesaData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDisponivel(Long mesaId, LocalDateTime dataHora) {
        // Consideramos um intervalo de 2 horas para cada reserva
        LocalDateTime fimReserva = dataHora.plusHours(2);

        return jpaMesaRepository.isDisponivel(mesaId, dataHora, fimReserva);
    }

    private MesaData convertToData(Mesa mesa) {
        MesaData data = new MesaData();
        data.setId(mesa.getId());
        data.setNumero(mesa.getNumero());
        data.setCapacidade(mesa.getCapacidade());

        if (mesa.getRestaurante() != null) {
            RestauranteData restauranteData = new RestauranteData();
            restauranteData.setId(mesa.getRestaurante().getId());
            data.setRestaurante(restauranteData);
        }

        // Não convertemos reservas aqui para evitar referências circulares
        // Elas serão carregadas sob demanda pelos métodos específicos

        return data;
    }
}