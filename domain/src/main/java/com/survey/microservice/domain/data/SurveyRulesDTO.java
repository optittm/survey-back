package com.survey.microservice.domain.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class SurveyRulesDTO {
    @JsonProperty("is_activated")
    public boolean isActivated;
    @JsonProperty("delay_before_reanswer")
    public int delayBeforeReAnswer;
    @JsonProperty("delay_to_answer")
    public int delayToAnswer;
    @JsonProperty("ratio_display")
    public int ratioDisplay;

}
