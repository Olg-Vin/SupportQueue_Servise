package org.vinio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(name = "category")
    private String category;
    @Column(name = "subject", nullable = false)
    private String subject;
    @Column(name = "body", columnDefinition = "TEXT",
            nullable = false)
    private String body;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column(name = "status", nullable = false)
    private String status;
    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL)
    private ReplyEntity reply;
}




