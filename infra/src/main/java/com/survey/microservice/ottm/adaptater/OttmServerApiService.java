package com.survey.microservice.ottm.adaptater;

import com.survey.microservice.domain.data.KeyDTO;
import com.survey.microservice.domain.data.ProjectDTO;
import com.survey.microservice.domain.data.SurveyRulesDTO;
import com.survey.microservice.domain.data.UserCommentDTO;
import com.survey.microservice.domain.ports.spi.IOttmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OttmServerApiService implements IOttmRepository {

    @Value("${environments.dev.urlApiServer}")
    private String URL_OTTM_SERVER;
    private static final String URL_SURVEY_RULES = "/rules";
    private static final String URL_SURVEY = "/surveys";
    private static final String URL_COMMENT = "/comments";
    private static final String URL_PROJECT = "/projects";
    private static final String URL_FEATURE = "/features";
    private static final String URL_TIME_LAST_COMMENT = "/times";
    private static final String URL_TIMESTAMP_KEY = "/timestamps";
    private static final String URL_DEFAULT_SURVEY = "/defaults";

    private static final Logger logger = LoggerFactory.getLogger(OttmServerApiService.class);

    private final RestTemplate restTemplate;

    @Autowired
    public OttmServerApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<LocalDateTime> getLastTimeUserAnswered(UUID userId, String featureUrl){
        String urlLastTimeUserAnswered = UriComponentsBuilder.fromHttpUrl(URL_OTTM_SERVER + URL_SURVEY + URL_TIME_LAST_COMMENT)
                .queryParam("user_id", userId.toString())
                .queryParam("feature_url", featureUrl)
                .encode()
                .toUriString();

        Optional<LocalDateTime> rules = Optional.empty();
        try{
            ResponseEntity<LocalDateTime> dateResponsesEntity = restTemplate.getForEntity(urlLastTimeUserAnswered,
                    LocalDateTime.class);
            rules = Optional.ofNullable(dateResponsesEntity.getBody());
        }catch(HttpClientErrorException error){
            logger.info("Error for getting last time user answered !");
        }


        return rules;
    }

    @Override
    public Optional<ProjectDTO> getProjectFromFeature(String featureUrl){
        String surveyRulesUrl = UriComponentsBuilder.fromHttpUrl(URL_OTTM_SERVER + URL_PROJECT + URL_FEATURE)
                .queryParam("feature_url", featureUrl)
                .encode()
                .toUriString();
        Optional<ProjectDTO> project = Optional.empty();
        try{

            ResponseEntity<ProjectDTO> rulesResponsesEntity = restTemplate.getForEntity(surveyRulesUrl, ProjectDTO.class);
            project = Optional.ofNullable(rulesResponsesEntity.getBody());
            logger.info("Rules get: " + rulesResponsesEntity.getBody());
        }catch(HttpClientErrorException error){
            logger.info("Error for getting project from features !");
        }
        return project;
    }

    @Override
    public Optional<SurveyRulesDTO> getSurveyRules(String featureUrl){
        String surveyRulesUrl = UriComponentsBuilder.fromHttpUrl(URL_OTTM_SERVER + URL_SURVEY + URL_SURVEY_RULES)
                .queryParam("feature_url", featureUrl)
                .encode()
                .toUriString();
        Optional<SurveyRulesDTO> surveyRules = Optional.empty();
        try{
            ResponseEntity<SurveyRulesDTO> projectResponsesEntity = restTemplate.getForEntity(surveyRulesUrl, SurveyRulesDTO.class);
            surveyRules = Optional.ofNullable(projectResponsesEntity.getBody());
            logger.info("Project get: " + projectResponsesEntity.getBody());
        }catch(HttpClientErrorException error){
            logger.info("Error for getting survey rules !");
        }
        return surveyRules;
    }

    @Override
    public Optional<String> getSecretKey(){
        logger.info("Try to get encoded Key in the Ottm API Server");
        Optional<String>  secretKey = Optional.empty();
        try{
            ResponseEntity<KeyDTO> secretKeyResponsesEntity = restTemplate.getForEntity(URL_OTTM_SERVER + URL_SURVEY +
                            URL_TIMESTAMP_KEY,
                    KeyDTO.class);
            secretKey = Optional.of(secretKeyResponsesEntity.getBody().key);
        }catch(HttpClientErrorException error){
            if(error.getStatusCode() == HttpStatus.NOT_FOUND){
                logger.info("Encoded Key not found in the Ottm API Server");
            }
        }
        return secretKey;
    }

    @Override
    public void sendNewComment(UserCommentDTO comment) {
        ResponseEntity<Void> commentResponseEntity = restTemplate.postForEntity(URL_OTTM_SERVER + URL_SURVEY
                                                                                + URL_COMMENT,
                                                                                 comment, Void.class);
        if(commentResponseEntity.getStatusCode() != HttpStatus.OK){
            logger.info("Error on send new comment");
        }
    }

    @Override
    public void saveEncodedKey(String key) {
        logger.info("Try to save Encoded Key in the Ottm API Server: " + key);
        try{
            KeyDTO keyDTO = new KeyDTO(key);
            restTemplate.postForObject(URL_OTTM_SERVER + URL_SURVEY + URL_TIMESTAMP_KEY,
                    keyDTO, Void.class);
        }catch(HttpClientErrorException error){
            logger.info("Error on save encoded key");
        }
    }

    @Override
    public Optional<SurveyRulesDTO> getDefaultSurveyRuleFromProject(ProjectDTO project) {
        String surveyRulesUrl =  URL_OTTM_SERVER + URL_SURVEY + URL_PROJECT + "/" + project.id + URL_DEFAULT_SURVEY;

        Optional<SurveyRulesDTO> rules = Optional.empty();
        try{
            ResponseEntity<SurveyRulesDTO> rulesResponsesEntity = restTemplate.getForEntity(surveyRulesUrl,
                    SurveyRulesDTO.class);
            rules = Optional.ofNullable(rulesResponsesEntity.getBody());
            logger.info("Rules get: " + rulesResponsesEntity.getBody());
        }catch(HttpClientErrorException error){
            logger.info("Error for getting survey rules !");
        }
        return rules;
    }
}
