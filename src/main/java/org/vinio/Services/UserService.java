package org.vinio.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.entities.UserEntity;
import org.vinio.repositories.UserRepository;

import java.util.List;

@Service
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity user = userRepository.save(userMapper.convertToEntity(userDTO));
        log.info("[service] Save user with id " + user.getUserId());
        return userMapper.convertToDto(user);
    }

    public UserDTO getUser(Long id) {
        return userMapper.convertToDto(userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with id " + id + " not found");
                    return new RuntimeException("User with id " + id + " not found");
                }));
    }

    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(userMapper::convertToDto).toList();
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("[service] Update user with id " + id);
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with id " + id + " not found");
                    return new RuntimeException("User with id " + id + " not found");
                });
        existingUser.setName(userDTO.getName());
        UserEntity updatedUser = userRepository.save(existingUser);
        return userMapper.convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        log.info("[service] Delete user with id " + id);
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with id " + id + " not found");
                    return new RuntimeException("User with id " + id + " not found");
                });
        userRepository.delete(existingUser);
        log.info("User with id " + id + " successfully deleted");
    }
}