package org.vinio.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
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

    public void saveReply(ReplyDTO replyDTO) {
        log.info("Save reply");
        replyRepository.save(replyMapper.convertToEntity(replyDTO));
    }

    public ReplyDTO getReply(Long id) {
        return replyMapper.convertToDto(replyRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Reply with id " + id + " not found");
                    return new RuntimeException("Reply with id " + id + " not found");
                }));
    }

//    TODO надо будет менять поле status. Удалять и полностью обновлять не требуется
}
