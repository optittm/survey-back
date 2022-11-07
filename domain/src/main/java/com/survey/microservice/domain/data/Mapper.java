package com.survey.microservice.domain.data;

import java.time.LocalDateTime;
import java.util.UUID;


public class Mapper {

    private static Mapper instance = null;

    private Mapper(){}

    public static Mapper getInstance(){
        if(instance == null){
            instance = new Mapper();
        }
        return instance;
    }

    public UserCommentDTO toDto(UserComment comment, LocalDateTime time, UUID id){
        return new UserCommentDTO(
                comment.getRating(),
                comment.getDescription(),
                comment.getFeatureUrl(),
                time,
                id
        );
    }

    public UserComment toUserComment(UserCommentDTO dto){
        return new UserComment(
            dto.getRating(),
            dto.getDescription(),
            dto.getFeatureUrl()
        );
    }
}
