package com.postech.gourmet.adapters.mapper;

import com.postech.gourmet.adapters.dto.*;
import com.postech.gourmet.domain.entities.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço unificado de mapeamento que configura o ModelMapper
 * e fornece métodos para converter entre entidades e DTOs
 */
public class MapperService {

    private final ModelMapper modelMapper;

    public MapperService() {
        this.modelMapper = configureModelMapper();
    }

    /**
     * Configura o ModelMapper com todos os mapeamentos personalizados
     */
    private ModelMapper configureModelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Configurações gerais
        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true)
                .setFieldMatchingEnabled(true);

        // Configurar conversores para cada tipo de mapeamento
        configureRestauranteMapper(mapper);
        configureMesaMapper(mapper);
        configureReservaMapper(mapper);
        configureAvaliacaoMapper(mapper);

        return mapper;
    }

    /**
     * Configuração do mapeamento para Restaurante
     */
    private void configureRestauranteMapper(ModelMapper mapper) {
        // Restaurante -> RestauranteDTO
        Converter<Restaurante, RestauranteDTO> toDto = ctx -> {
            Restaurante source = ctx.getSource();
            RestauranteDTO destination = new RestauranteDTO();

            if (source.getId() != null) destination.setId(source.getId());
            if (source.getNome() != null) destination.setNome(source.getNome());
            if (source.getTipoCozinha() != null) destination.setTipoCozinha(source.getTipoCozinha());
            if (source.getEndereco() != null) destination.setEndereco(source.getEndereco());
            if (source.getHorarioFuncionamento() != null)
                destination.setHorarioFuncionamento(source.getHorarioFuncionamento());
            if (source.getCapacidade() != null) destination.setCapacidade(source.getCapacidade());

            // Captura a média de avaliações de forma segura
            try {
                Double media = source.calcularMediaAvaliacoes();
                destination.setMediaAvaliacoes(media);
            } catch (Exception e) {
                // Se o método não existir ou lançar exceção, ignoramos
            }

            return destination;
        };

        mapper.createTypeMap(Restaurante.class, RestauranteDTO.class)
                .setConverter(toDto);
    }

    /**
     * Configuração do mapeamento para Mesa
     */
    private void configureMesaMapper(ModelMapper mapper) {
        // Mesa -> MesaDTO
        Converter<Mesa, MesaDTO> toDto = ctx -> {
            Mesa source = ctx.getSource();
            MesaDTO destination = new MesaDTO();

            if (source.getId() != null) destination.setId(source.getId());
            if (source.getNumero() != null) destination.setNumero(source.getNumero());
            if (source.getCapacidade() != null) destination.setCapacidade(source.getCapacidade());
            if (source.getStatus() != null) destination.setStatus(source.getStatus());

            // Mapeia o ID do restaurante de forma segura
            if (source.getRestaurante() != null && source.getRestaurante().getId() != null) {
                destination.setRestauranteId(source.getRestaurante().getId());
            }

            return destination;
        };

        mapper.createTypeMap(Mesa.class, MesaDTO.class)
                .setConverter(toDto);
    }

    /**
     * Configuração do mapeamento para Reserva
     */
    private void configureReservaMapper(ModelMapper mapper) {
        // Reserva -> ReservaDTO
        Converter<Reserva, ReservaDTO> toDto = ctx -> {
            Reserva source = ctx.getSource();
            ReservaDTO destination = new ReservaDTO();

            if (source.getId() != null) destination.setId(source.getId());
            if (source.getDataHora() != null) destination.setDataHora(source.getDataHora());
            if (source.getNumPessoas() != null) destination.setNumPessoas(source.getNumPessoas());
            if (source.getStatus() != null) destination.setStatus(source.getStatus().toString());

            // Mapeia IDs relacionados de forma segura
            if (source.getMesa() != null && source.getMesa().getId() != null) {
                destination.setMesaId(source.getMesa().getId());
            }

            if (source.getUsuario() != null && source.getUsuario().getId() != null) {
                destination.setUsuarioId(source.getUsuario().getId());
            }

            return destination;
        };

        mapper.createTypeMap(Reserva.class, ReservaDTO.class)
                .setConverter(toDto);
    }

    /**
     * Configuração do mapeamento para Avaliação
     */
    private void configureAvaliacaoMapper(ModelMapper mapper) {
        // Avaliacao -> AvaliacaoDTO
        Converter<Avaliacao, AvaliacaoDTO> toDto = ctx -> {
            Avaliacao source = ctx.getSource();
            AvaliacaoDTO destination = new AvaliacaoDTO();

            if (source.getId() != null) destination.setId(source.getId());
            if (source.getNota() != null) destination.setNota(source.getNota());
            if (source.getComentario() != null) destination.setComentario(source.getComentario());
            if (source.getDataAvaliacao() != null) destination.setDataAvaliacao(source.getDataAvaliacao());

            // Mapeia IDs relacionados de forma segura
            if (source.getRestaurante() != null && source.getRestaurante().getId() != null) {
                destination.setRestauranteId(source.getRestaurante().getId());
            }

            if (source.getUsuario() != null && source.getUsuario().getId() != null) {
                destination.setUsuarioId(source.getUsuario().getId());
            }

            return destination;
        };

        mapper.createTypeMap(Avaliacao.class, AvaliacaoDTO.class)
                .setConverter(toDto);
    }

    /**
     * Converte uma entidade para um DTO
     */
    public <D, T> D toDTO(T entity, Class<D> dtoClass) {
        if (entity == null) {
            return null;
        }
        return modelMapper.map(entity, dtoClass);
    }

    /**
     * Converte um DTO para uma entidade
     */
    public <D, T> T toEntity(D dto, Class<T> entityClass) {
        if (dto == null) {
            return null;
        }
        return modelMapper.map(dto, entityClass);
    }

    /**
     * Converte uma lista de entidades para uma lista de DTOs
     */
    public <D, T> List<D> toListDTO(List<T> entityList, Class<D> dtoClass) {
        if (entityList == null || entityList.isEmpty()) {
            return List.of();
        }
        return entityList.stream()
                .map(entity -> toDTO(entity, dtoClass))
                .collect(Collectors.toList());
    }

    /**
     * Converte uma lista de DTOs para uma lista de entidades
     */
    public <D, T> List<T> toListEntity(List<D> dtoList, Class<T> entityClass) {
        if (dtoList == null || dtoList.isEmpty()) {
            return List.of();
        }
        return dtoList.stream()
                .map(dto -> toEntity(dto, entityClass))
                .collect(Collectors.toList());
    }

    /**
     * Atualiza uma entidade existente com os dados de um DTO
     */
    public <D, T> T updateEntityFromDTO(D dto, T entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        modelMapper.map(dto, entity);
        return entity;
    }

    /**
     * Fornece acesso ao ModelMapper configurado para casos especiais
     */
    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }
}