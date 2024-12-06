package org.vinio.controllers.rest.v1;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
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
 *     Создание нового пользователя
 *     Получение данных одного или всех пользователей
 *     Обновление данных пользователя
 *     Удаление пользователя
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
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class UserController implements UserApi {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    @Autowired
    public UserController(UserService userService, UserMapper userMapper, ModelMapper modelMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.modelMapper = modelMapper;
    }

    /**
     * Получение данных о пользователе
     * Этот эндпоинт позволяет получить из БД информацию о пользователе по его id
     *
     * @param id - идентификатор пользователя в БД
     * @return возвращает ResponseEntity с DTO объектом пользователя и статусом ответа
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserDTO user = userService.getUser(id);
        UserResponseDTO userResponseDTO =
                userMapper.convertToResponse(user, createActions(id), createLinks(id));
        return new ResponseEntity<>((userResponseDTO), HttpStatus.OK);
    }

    /**
     * Создание динамических ссылок
     * Этот эндпоинт позволяет получить ссылки на активности для пользователя
     *
     * @param id - идентификатор пользователя в БД
     * @return возвращает список доступных ссылок (self, update)
     * */
    private List<Link> createLinks (Long id) {
        Class<UserController> controllerClass = UserController.class;
        Link selfLink = linkTo(methodOn(controllerClass).getUserById(id))
                .withSelfRel().withType("self");
        Link updateLink = linkTo(methodOn(controllerClass).updateUser(id, null))
                .withRel("update").withType("update");
        Link deleteLink = linkTo(methodOn(controllerClass).deleteUser(id))
                .withRel("delete").withType("delete");
        return List.of(selfLink, updateLink, deleteLink);
    }

    /**
     * Создание динамических ссылок
     * Этот эндпоинт позволяет получить ссылки на действия, связанные с пользователем
     *
     * @param id - идентификатор пользователя в БД
     * @return возвращает список доступных действий (message)
     * */
    private List<Link> createActions (Long id) {
        Link messageLink = linkTo(methodOn(MessageController.class).getMessagesByUserId(id))
                .withRel("messages").withType("UserMessages");
        return List.of(messageLink);
    }

    /**
     * Получение списка всех пользователей
     * Этот эндпоинт позволяет получить из БД информацию обо всех пользователях
     *
     * @return возвращает ResponseEntity со списком DTO объектов пользователей и статусом ответа
     */
    @Override
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        List<UserResponse> userResponseDTOs = users.stream()
                .map(user -> {
                    Long userId = user.getUserId();
                    return userMapper.convertToResponse(
                            user, createActions(userId), createLinks(userId));
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(userResponseDTOs, HttpStatus.OK);
    }


    /**
     * Создание нового пользователя.
     * Этот эндпоинт позволяет сохранить данные о пользователе в базе данных.
     *
     * @param user объект AddUserRequest, содержащий данные нового пользователя. Передаётся в теле запроса.
     * @return ResponseEntity, содержащий сохранённый объект AddUserRequest и HTTP-статус ответа.
     */

    @Override
    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid AddUserRequest user) {
        log.info("[endpoint] создание нового пользователя");
        UserDTO userDTO = userService.saveUser(modelMapper.map(user, UserDTO.class));
        return new ResponseEntity<>(userMapper.convertToResponse(userDTO), HttpStatus.OK);
    }

    /**
     * Обновление данных пользователя.
     * Этот эндпоинт позволяет обновить информацию о пользователе с заданным идентификатором.
     *
     * @param id идентификатор пользователя, данные которого требуется обновить.
     * @param user объект AddUserRequest, содержащий обновлённые данные пользователя.
     *                       Передаётся в теле запроса.
     * @return ResponseEntity, содержащий обновлённый объект AddUserRequest и HTTP-статус OK (200).
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id,
                                                   @RequestBody @Valid AddUserRequest user) {
        log.info("[endpoint] обновление пользователя с id " + id);
        UserDTO updatedUser = userService.updateUser(id, modelMapper.map(user, UserDTO.class));
        return new ResponseEntity<>(userMapper.convertToResponse(updatedUser), HttpStatus.OK);
    }


    /**
     * Удаление пользователя.
     * Этот эндпоинт позволяет удалить пользователя с заданным идентификатором.
     *
     * @param id идентификатор пользователя, которого требуется удалить.
     * @return ResponseEntity с HTTP-статусом NO_CONTENT (204) в случае успешного удаления.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        log.info("[endpoint] удаление пользователя с id " + id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}



