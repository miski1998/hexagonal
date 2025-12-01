package com.tecsup.example.hexagonal.infrastructure.adapter.output.persistance.mapper;


import com.tecsup.example.hexagonal.domain.model.User;
import com.tecsup.example.hexagonal.infrastructure.adapter.input.rest.dto.UserRequest;
import com.tecsup.example.hexagonal.infrastructure.adapter.input.rest.dto.UserResponse;
import com.tecsup.example.hexagonal.infrastructure.adapter.output.persistance.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Convert User domain to UserEntity
     * @param domain
     * @return
     */
    UserEntity toEntity(User domain);

    /**
     * Convert UserEntity to User domain
     * @param entity
     * @return
     */
    User toDomain(UserEntity entity);


    @Mapping(target = "id", ignore = true) // New users don't have ID
    @Mapping(target = "enabled", ignore = true) // Will be set by service
    @Mapping(target = "role", ignore = true) // Will be set by service
    User toDomain(UserRequest request);

    UserResponse toResponse(User createUser);
}