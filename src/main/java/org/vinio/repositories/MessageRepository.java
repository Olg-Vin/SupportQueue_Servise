package org.vinio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vinio.entities.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
