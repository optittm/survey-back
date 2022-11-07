package com.survey.microservice.domain.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ProjectDTO {
    public String id;
    public String name;
    public String description;
    @JsonProperty("is_active")
    public String isActive;
}
