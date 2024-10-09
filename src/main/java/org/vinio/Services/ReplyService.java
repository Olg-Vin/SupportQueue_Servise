package org.vinio.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.entities.ReplyEntity;
import org.vinio.repositories.ReplyRepository;

@Service
@Log4j2
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final ReplyMapper replyMapper;
    @Autowired
    public ReplyService(ReplyRepository replyRepository, ReplyMapper replyMapper) {
        this.replyRepository = replyRepository;
        this.replyMapper = replyMapper;
    }

    public ReplyDTO saveReply(ReplyDTO replyDTO) {
        ReplyEntity reply = replyRepository.save(replyMapper.convertToEntity(replyDTO));
        log.info("[service] Save reply with id " + reply.getReplyId());
        return replyMapper.convertToDto(reply);
    }

    public ReplyDTO getReply(Long id) {
        return replyMapper.convertToDto(replyRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Reply with id " + id + " not found");
                    return new RuntimeException("Reply with id " + id + " not found");
                }));
    }

    public ReplyDTO updateReply(Long id, ReplyDTO replyDTO) {
        log.info("[service] Update reply with id " + id);
        ReplyEntity existingReply = replyRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Reply with id " + id + " not found");
                    return new RuntimeException("Reply with id " + id + " not found");
                });
        existingReply.setBody(replyDTO.getBody());
        existingReply.setSentAt(replyDTO.getSentAt());
        existingReply.setStatus(replyDTO.getStatus());
        ReplyEntity updatedReply = replyRepository.save(existingReply);
        return replyMapper.convertToDto(updatedReply);
    }

    public void deleteReply(Long id) {
        log.info("[service] Delete reply with id " + id);
        ReplyEntity existingReply = replyRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Reply with id " + id + " not found");
                    return new RuntimeException("Reply with id " + id + " not found");
                });
        replyRepository.delete(existingReply);
        log.info("Reply with id " + id + " successfully deleted");
    }

    public ReplyDTO getReplyByMessageId(Long messageId) {
        ReplyEntity reply = replyRepository.findByMessage_MessageId(messageId);
        log.info("[service] get reply for message with id " + messageId);
        return replyMapper.convertToDto(reply);
    }
}
