package com.survey.microservice.application.configuration;

import com.survey.microservice.domain.ports.api.ICommentService;
import com.survey.microservice.domain.ports.api.IProjectService;
import com.survey.microservice.domain.ports.api.ISurveyService;
import com.survey.microservice.domain.ports.api.ITimestampService;
import com.survey.microservice.domain.ports.spi.IOttmRepository;
import com.survey.microservice.domain.service.CommentService;
import com.survey.microservice.domain.service.ProjectService;
import com.survey.microservice.domain.service.SurveyService;
import com.survey.microservice.domain.service.TimestampService;
import com.survey.microservice.domain.utils.AESCrypt;
import com.survey.microservice.domain.utils.ICrypt;
import com.survey.microservice.ottm.adaptater.OttmServerApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MicroserviceConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IOttmRepository ottmRepository() {
        return new OttmServerApiService(getRestTemplate());
    }

    @Bean
    public ICrypt crypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return new AESCrypt();
    }

    @Bean
    public ITimestampService timestampService() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return new TimestampService(crypt(), ottmRepository());
    }

    @Bean
    public ISurveyService surveyService(){
        return new SurveyService(ottmRepository());
    }

    @Bean
    public ICommentService commentService(){
        return new CommentService(ottmRepository());
    }

    @Bean
    public IProjectService projectService(){
        return new ProjectService(ottmRepository());
    }

}
