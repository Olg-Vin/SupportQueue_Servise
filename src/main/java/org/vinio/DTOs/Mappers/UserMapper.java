package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.UserDTO;
import org.vinio.controllers.responseDTO.UserResponseDTO;
import org.vinio.entities.UserEntity;

/**
 * Класс-мапер для сущности user
 */
@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }

    public UserEntity convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }


    public UserResponseDTO convertToResponse(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserResponseDTO.class);
    }
}
