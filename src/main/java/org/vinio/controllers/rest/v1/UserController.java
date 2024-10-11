package org.vinio.controllers.rest.v1;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.Services.UserService;
import org.vinio.controllers.responseDTO.UserResponseDTO;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/users")
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class UserController {
    private UserService userService;
    private UserMapper userMapper;
    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDTO>> getUserById(@PathVariable("id") Long id) {
        UserDTO user = userService.getUser(id);
        UserResponseDTO userResponseDTO = userMapper.convertToResponse(user, createActions(id), createLinks(id));
        return new ResponseEntity<>(EntityModel.of(userResponseDTO), HttpStatus.OK);
    }
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
    private List<Link> createActions (Long id) {
        Link messageLink = linkTo(methodOn(MessageController.class).getMessagesByUserId(id))
                .withRel("messages").withType("UserMessages");
        return List.of(messageLink);
    }
    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        log.info("[endpoint] создание нового пользователя");
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO updatedUserDTO) {
        log.info("[endpoint] обновление пользователя с id " + id);
        UserDTO updatedUser = userService.updateUser(id, updatedUserDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        log.info("[endpoint] удаление пользователя с id " + id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}



