package org.vinio.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.repositories.UserRepository;

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

    public void saveUser(UserDTO userDTO) {
        log.info("Save user");
        userRepository.save(userMapper.convertToEntity(userDTO));
    }

    public UserDTO getUser(Long id) {
        return userMapper.convertToDto(userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User with id " + id + " not found");
                    return new RuntimeException("User with id " + id + " not found");
                }));
    }

//    TODO Удалять и полностью обновлять не требуется
}