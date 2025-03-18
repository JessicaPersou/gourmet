package com.postech.gourmet.gateways;

import com.postech.gourmet.domain.entities.Reserva;
import com.postech.gourmet.domain.repositories.ReservaRepository;
import com.postech.gourmet.gateways.data.MesaData;
import com.postech.gourmet.gateways.data.ReservaData;
import com.postech.gourmet.gateways.data.UsuarioData;
import com.postech.gourmet.gateways.jpa.JpaReservaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> findByMesaRestauranteId(Long restauranteId) {
        return jpaReservaRepository.findByMesaRestauranteId(restauranteId)
                .stream()
                .map(ReservaData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> findReservasAtivasByMesaIdAndIntervaloTempo(
            Long mesaId, LocalDateTime inicio, LocalDateTime fim) {
        return jpaReservaRepository.findReservasAtivasByMesaIdAndIntervaloTempo(mesaId, inicio, fim)
                .stream()
                .map(ReservaData::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByMesaRestauranteIdAndData(Long restauranteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return jpaReservaRepository.countByMesaRestauranteIdAndData(restauranteId, dataInicio, dataFim);
    }

    private ReservaData convertToData(Reserva reserva) {
        ReservaData data = new ReservaData();
        data.setId(reserva.getId());
//        data.setUsuario(reserva.getUsuario());
        data.setDataHora(reserva.getDataHora());
        data.setStatus(reserva.getStatus().toString());

        if (reserva.getMesa() != null) {
            MesaData mesaData = new MesaData();
            mesaData.setId(reserva.getMesa().getId());
            data.setMesa(mesaData);
        }

        if (reserva.getUsuario() != null) {
            UsuarioData usuarioData = new UsuarioData();
            usuarioData.setId(reserva.getUsuario().getId());
            data.setUsuario(usuarioData);
        }

        return data;
    }
}