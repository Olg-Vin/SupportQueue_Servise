package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.UserDTO;
import org.vinio.controllers.responseDTO.UserQLDto;
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

    public UserEntity convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }

    // Преобразование UserDTO в UserQLDto с добавлением действий и ссылок
    public UserQLDto convertToQLDto(UserDTO userDTO, List<Link> actions, List<Link> links) {
        UserQLDto userQLDto = modelMapper.map(userDTO, UserQLDto.class);
        userQLDto.setActions(actions);  // Добавляем действия
        userQLDto.setLinks(links);      // Добавляем ссылки
        return userQLDto;
    }
    public UserQLDto convertToQLDto(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserQLDto.class);
    }

    // Преобразование UserQLDto в UserDTO (если потребуется)
    public UserDTO convertToDto(UserQLDto userQLDto) {
        return modelMapper.map(userQLDto, UserDTO.class);
    }
}
