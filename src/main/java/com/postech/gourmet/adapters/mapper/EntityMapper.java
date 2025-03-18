package com.postech.gourmet.adapters.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    private final ModelMapper modelMapper;

    public EntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <D, T> D mapTo(T entity, Class<D> dtoClass) {
        if (entity == null) {
            return null;
        }
        return modelMapper.map(entity, dtoClass);
    }

    public <D, T> List<D> mapToList(List<T> entities, Class<D> dtoClass) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(entity -> mapTo(entity, dtoClass))
                .collect(Collectors.toList());
    }

    public <D, T> T updateEntityFromDTO(D dto, T entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        modelMapper.map(dto, entity);
        return entity;
    }
}