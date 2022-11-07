package com.survey.microservice.domain.service;

import com.survey.microservice.domain.data.ProjectDTO;
import com.survey.microservice.domain.data.SurveyRulesDTO;
import com.survey.microservice.domain.ports.api.ISurveyService;
import com.survey.microservice.domain.ports.spi.IOttmRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class SurveyService implements ISurveyService {

    IOttmRepository ottmApiRepository;

    public SurveyService(IOttmRepository repository){
        this.ottmApiRepository = repository;
    }

    public Optional<SurveyRulesDTO> getSurveyRules(String featureUrl){
        Optional<SurveyRulesDTO> surveyRules = ottmApiRepository.getSurveyRules(featureUrl);
        if (surveyRules.isEmpty()) {
            Optional<ProjectDTO> project = ottmApiRepository.getProjectFromFeature(featureUrl);
            if(project.isPresent()){
                return ottmApiRepository.getDefaultSurveyRuleFromProject(project.get());
            }
        }
        return surveyRules;
    }

    public Optional<LocalDateTime> findLastTimeUserAnswered(String id, String featureUrl){
        UUID userId = UUID.fromString(id);
        return ottmApiRepository.getLastTimeUserAnswered(userId, featureUrl);
    }

    public String generateUserId(){
        return UUID.randomUUID().toString();
    }

    public boolean checkSurveyRules(Optional<SurveyRulesDTO> rules, Optional<LocalDateTime> lastTimeAnswer) {
        if(rules.isEmpty()){
            return false;
        }
        if(!checkRules(rules.get())){
            return false;
        }
        return lastTimeAnswer.map(localDateTime -> checkUserRules(rules.get().delayBeforeReAnswer, localDateTime))
                .orElse(true);
    }

    public boolean checkDelayToAnswer(SurveyRulesDTO rules, LocalDateTime timeWhenAnswered){
        long actualTimeInMillis = System.currentTimeMillis();
        long delay = rules.delayToAnswer;
        return actualTimeInMillis - getTimeInMillis(timeWhenAnswered) <= delay;
    }

    private boolean checkRules(SurveyRulesDTO rules){
        if(!rules.isActivated){
            return false;
        }
        return getRandomNumber(1,100) <= rules.ratioDisplay;
    }

    private boolean checkUserRules(int DelayBeforeReAnswer, LocalDateTime lastTimeAnswer){
        long diffInMillis = Math.abs(new Date().getTime() - getTimeInMillis(lastTimeAnswer));
        return diffInMillis <= DelayBeforeReAnswer;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private long getTimeInMillis(LocalDateTime ldt){
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

}
