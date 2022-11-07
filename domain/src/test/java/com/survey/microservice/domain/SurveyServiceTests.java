package com.survey.microservice.domain;

import com.survey.microservice.domain.data.SurveyRulesDTO;
import com.survey.microservice.domain.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SurveyServiceTests {

    private static final int DELAY_TO_ANSWER = 12000000;
    private static final int DELAY_TO_REANSWER = 5;
    private SurveyService surveyService;

    @BeforeEach
    void init() {
        surveyService = new SurveyService(null);
    }

    @Test
    void testCheckDelayToAnswer(){
        SurveyRulesDTO rules = new SurveyRulesDTO();
        rules.delayToAnswer = DELAY_TO_ANSWER;
        String dateInString = "1982-08-31 10:20:56";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateInString, formatter);
        boolean result = surveyService.checkDelayToAnswer(rules,dateTime);
        assertEquals(false,result);

        dateTime = LocalDateTime.now();
        result = surveyService.checkDelayToAnswer(rules, dateTime);
        assertEquals(true,result);
    }

    @Test
    void testSurveyRules(){
        SurveyRulesDTO rules = new SurveyRulesDTO();
        rules.isActivated = false;
        LocalDateTime dateTime = LocalDateTime.now();
        boolean result = surveyService.checkSurveyRules(Optional.of(rules), Optional.of(dateTime));
        assertEquals(false,result);

        rules.isActivated = true;
        result = surveyService.checkSurveyRules(Optional.of(rules), Optional.of(dateTime));

        assertEquals(false,result);

        rules.isActivated = true;
        rules.delayBeforeReAnswer = DELAY_TO_REANSWER;
        result = surveyService.checkSurveyRules(Optional.of(rules), Optional.of(dateTime));
        assertEquals(false, result);

    }


}
