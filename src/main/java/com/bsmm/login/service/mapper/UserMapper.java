package com.bsmm.login.service.mapper;

import com.bsmm.login.domain.UserEntity;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(imports = {UUID.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    UserEntity toEntity(UserSignup dto);

    @Mapping(target = "roles", ignore = true)
    UserDTO toDto(UserEntity entity);

}
