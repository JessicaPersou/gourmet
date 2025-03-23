package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.enums.StatusReserva;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.gateways.data.ReservaData;
import com.postech.gourmet.gateways.data.RestauranteData;
import com.postech.gourmet.gateways.data.UsuarioData;
import com.postech.gourmet.gateways.jpa.JpaReservaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservaRepositoryImpl implements ReservaRepository {
    private final JpaReservaRepository jpaReservaRepository;

    public ReservaRepositoryImpl(JpaReservaRepository jpaReservaRepository) {
        this.jpaReservaRepository = jpaReservaRepository;
    }

    @Override
    public Reserva save(Reserva reserva) {
        ReservaData data = convertToData(reserva);
        return jpaReservaRepository.save(data).toDomain();
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return jpaReservaRepository.findById(id).map(ReservaData::toDomain);
    }

    @Override
    public List<Reserva> findAll() {
        return jpaReservaRepository.findAll()
                .stream()
                .map(ReservaData::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaReservaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaReservaRepository.deleteById(id);
    }

    @Override
    public List<Reserva> findByUsuarioId(Long usuarioId) {
        return jpaReservaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ReservaData::toDomain)
                .toList();
    }

    private ReservaData convertToData(Reserva reserva) {
        ReservaData data = new ReservaData();
        data.setId(reserva.getId());
        data.setCliente(reserva.getCliente());
        data.setDataHora(reserva.getDataHora());
        data.setNumeroPessoas(reserva.getNumeroPessoas());

        if (reserva.getStatus() != null) {
            data.setStatus(reserva.getStatus().toString());
        } else {
            data.setStatus(StatusReserva.PENDENTE.toString());
        }

        if (reserva.getRestaurante() != null) {
            RestauranteData restauranteData = new RestauranteData();
            restauranteData.setId(reserva.getRestaurante().getId());
            restauranteData.setNome(reserva.getRestaurante().getNome());
            data.setRestaurante(restauranteData);
        }

        if (reserva.getUsuario() != null) {
            UsuarioData usuarioData = new UsuarioData();
            usuarioData.setId(reserva.getUsuario().getId());
            data.setUsuario(usuarioData);
        }

        return data;
    }
}