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

    private Long user;

    private String category;
    private String subject;
    private String body;
    private Date createdAt;
    private String status;

//    private List<ReplyDTO> replies;
}