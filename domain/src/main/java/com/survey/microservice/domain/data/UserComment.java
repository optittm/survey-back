package com.survey.microservice.domain.data;

public class UserComment {
    private String rating;
    private String description;
    private String featureUrl;

    public UserComment(){}

    public UserComment(String rating, String description, String featureUrl) {
        this.rating = rating;
        this.description = description;
        this.featureUrl = featureUrl;
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

    public void setDescription(String comment) {
        this.description = comment;
    }

    public String getFeatureUrl() {
        return featureUrl;
    }

    public void setFeatureUrl(String featureUrl) {
        this.featureUrl = featureUrl;
    }
}
