package org.vinio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinio.entities.MessageEntity;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    MessageEntity findByReply_ReplyId(Long replyId);
    List<MessageEntity> findByUser_UserId(Long replyId);
}
