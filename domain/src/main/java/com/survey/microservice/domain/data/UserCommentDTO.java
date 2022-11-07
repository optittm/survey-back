package com.survey.microservice.domain.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserCommentDTO {
    @JsonProperty("rating")
    private String rating;
    @JsonProperty("description")
    private String description;
    @JsonProperty("feature_url")
    private String featureUrl;
    @JsonProperty("date")
    private LocalDateTime date;
    @JsonProperty("user_id")
    private UUID userId;

    public UserCommentDTO(String rating, String description, String featureUrl, LocalDateTime date, UUID userId) {
        this.rating = rating;
        this.description = description;
        this.featureUrl = featureUrl;
        this.date = date;
        this.userId = userId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatureUrl() {
        return featureUrl;
    }

    public void setFeatureUrl(String featureUrl) {
        this.featureUrl = featureUrl;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
