package org.vinio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vinio.entities.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
}

