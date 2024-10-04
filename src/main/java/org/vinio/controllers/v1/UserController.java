package org.vinio.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.ExceptionsHandler.ResourceNotFoundException;
import org.vinio.Services.UserService;
import org.vinio.controllers.responseDTO.UserResponseDTO;
import org.vinio.entities.UserEntity;
import org.vinio.repositories.UserRepository;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/{id}")
    public EntityModel<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Class<UserController> controllerClass = UserController.class;

        Link selfLink = linkTo(methodOn(controllerClass).getUserById(id)).withSelfRel();
        Link updateLink = linkTo(methodOn(controllerClass).updateUser(id, null)).withRel("update");
        Link deleteLink = linkTo(methodOn(controllerClass).deleteUser(id)).withRel("delete");
        Link messageLink = linkTo(methodOn(MessageController.class).getMessagesByUserId(id)).withRel("messages");

        UserDTO userDTO = userMapper.convertToDto(user);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userDTO, List.of(updateLink, deleteLink));

        return EntityModel.of(userResponseDTO, selfLink, messageLink);
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UserEntity updatedUser) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        updatedUser.setUserId(user.getUserId());
        return new ResponseEntity<>(userRepository.save(updatedUser), HttpStatus.OK);
    }
}
