package com.survey.microservice.domain.service;

import com.survey.microservice.domain.data.Mapper;
import com.survey.microservice.domain.data.UserComment;
import com.survey.microservice.domain.data.UserCommentDTO;
import com.survey.microservice.domain.ports.api.ICommentService;
import com.survey.microservice.domain.ports.spi.IOttmRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentService implements ICommentService {

    private Mapper mapper;
    private IOttmRepository ottmRepository;

    public CommentService(IOttmRepository ottmRepository){
        this.ottmRepository = ottmRepository;
        this.mapper = Mapper.getInstance();
    }


    public void sendComment(UserComment comment, String userId){
        UserCommentDTO dto = mapper.toDto(comment, LocalDateTime.now(),
                UUID.fromString(userId));
        ottmRepository.sendNewComment(dto);
    }
}
