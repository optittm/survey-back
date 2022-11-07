package com.survey.microservice.domain.ports.api;

import com.survey.microservice.domain.data.UserComment;

public interface ICommentService {

    void sendComment(UserComment comment, String userId);
}
