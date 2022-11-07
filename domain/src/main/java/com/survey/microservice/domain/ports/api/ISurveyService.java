package com.survey.microservice.domain.ports.api;

import com.survey.microservice.domain.data.SurveyRulesDTO;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ISurveyService {

     Optional<SurveyRulesDTO> getSurveyRules(String featureUrl);
     Optional<LocalDateTime> findLastTimeUserAnswered(String id, String featureUrl);
     boolean checkSurveyRules(Optional<SurveyRulesDTO> rules, Optional<LocalDateTime> lastTimeAnswer);
     String generateUserId();

    boolean checkDelayToAnswer(SurveyRulesDTO rules, LocalDateTime time);
}
