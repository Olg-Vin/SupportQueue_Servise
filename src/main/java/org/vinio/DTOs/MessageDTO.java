package org.vinio.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long messageId;
    private Long userId;
    private String category;
    private String subject;
    private int priority;
    private String body;
    private Date createdAt;
    private String status;
}