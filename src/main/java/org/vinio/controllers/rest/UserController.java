package org.vinio.controllers.rest;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.Services.UserService;
import org.vinio.controllers.Rest.UserApi;
import org.vinio.controllers.responseDTO.UserResponseDTO;
import org.vinio.dtos.request.AddUserRequest;
import org.vinio.dtos.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Контроллер для управления сущностью пользователя (User).
 * Этот класс содержит эндпоинты для выполнения CRUD-операций над пользователями,
 * включая:
 * Создание нового пользователя
 * Получение данных одного или всех пользователей
 * Обновление данных пользователя
 * Удаление пользователя
 * Все методы возвращают ответы в формате HAL (Hypertext Application Language)
 * для обеспечения гипермедиа-поддержки. Поддерживается обработка
 * и преобразование DTO объектов для взаимодействия с клиентами.
 *
 * @see UserService
 * @see UserMapper
 */
@Log4j2
@RestController
@RequestMapping("/users")
public class UserController implements UserApi {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserDTO user = userService.getUser(id);
        return ResponseEntity.ok(createUserEntityModel(user));
    }

    @Override
    @GetMapping
    public ResponseEntity<CollectionModel<UserResponse>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        List<UserResponse> userResponseDTOs = users.stream()
                .map(this::createUserEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                CollectionModel.of(userResponseDTOs).add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()),
                HttpStatus.OK
        );
    }

    @Override
    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid AddUserRequest user) {
        log.info("[endpoint] создание нового пользователя");
        UserDTO userDTO = userService.saveUser(modelMapper.map(user, UserDTO.class));
        return new ResponseEntity<>(createUserEntityModel(userDTO), HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id,
                                                   @RequestBody @Valid AddUserRequest user) {
        log.info("[endpoint] обновление пользователя с id " + id);
        UserDTO updatedUser = userService.updateUser(id, modelMapper.map(user, UserDTO.class));
        return new ResponseEntity<>(createUserEntityModel(updatedUser), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        log.info("[endpoint] удаление пользователя с id " + id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Создание динамических ссылок для пользователя.
     *
     * @param userDTO - DTO объекта пользователя.
     * @return EntityModel<UserDTO> с гипермедиа-ссылками.
     */
    private UserResponseDTO createUserEntityModel(UserDTO userDTO) {
        Link selfLink = linkTo(methodOn(UserController.class).getUserById(userDTO.getUserId())).withSelfRel();
        Link messagesLink = linkTo(methodOn(MessageController.class).getMessagesByUserId(userDTO.getUserId())).withRel("messages");

        Link deleteLink = linkTo(methodOn(UserController.class).deleteUser(userDTO.getUserId())).withRel("delete");
        Link updateLink = linkTo(methodOn(UserController.class).updateUser(userDTO.getUserId(), null)).withRel("update");

        UserResponseDTO model = modelMapper.map(userDTO, UserResponseDTO.class);
        model.add(selfLink, messagesLink);
        model.addActions(deleteLink, updateLink);

        return model;
    }
}
