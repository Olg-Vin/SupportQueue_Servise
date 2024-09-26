package org.vinio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vinio.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
