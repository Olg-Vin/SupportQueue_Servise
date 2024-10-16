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
@Table(name = "reply")
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;
    @OneToOne
    @JoinColumn(name = "message_id")
    private MessageEntity message;
    @Column(name = "body", nullable = false)
    private String body;
    @Column(name = "sent_at", nullable = false)
    private Date sentAt;
    @Column(name = "status", nullable = false)
    private String status;
}

