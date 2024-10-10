package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.UserDTO;
import org.vinio.controllers.responseDTO.UserResponseDTO;
import org.vinio.entities.UserEntity;

import java.util.List;

/**
 * Класс-мапер для сущности user
 * */
@Component
public class UserMapper {
    private ModelMapper modelMapper;
    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }
    // TODO в dto лежат лишь id на сущность, надо явно её привязать
    public UserEntity convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }

    public UserResponseDTO convertToResponse(UserDTO userDTO, List<Link> actions, List<Link> links) {
        UserResponseDTO userResponseDTO = modelMapper.map(userDTO, UserResponseDTO.class);
        userResponseDTO.setActions(actions);  // Добавляем действия
        userResponseDTO.setLinks(links);      // Добавляем ссылки
        return userResponseDTO;
    }

    public UserResponseDTO convertToResponse(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserResponseDTO.class);
    }
}
