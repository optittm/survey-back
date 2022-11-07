package com.survey.microservice.domain.ports.spi;

import com.survey.microservice.domain.data.ProjectDTO;
import com.survey.microservice.domain.data.SurveyRulesDTO;
import com.survey.microservice.domain.data.UserCommentDTO;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IOttmRepository {

    Optional<LocalDateTime> getLastTimeUserAnswered(UUID userId, String featureUrl);

    Optional<SurveyRulesDTO> getSurveyRules(String featureUrl);

    Optional<ProjectDTO> getProjectFromFeature(String featureUrl);

    Optional<String> getSecretKey();

    void sendNewComment(UserCommentDTO comment);

    void saveEncodedKey(String key);

    Optional<SurveyRulesDTO> getDefaultSurveyRuleFromProject(ProjectDTO project);
}
