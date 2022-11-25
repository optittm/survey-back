package com.survey.microservice.application;

import com.survey.microservice.domain.data.ProjectDTO;
import com.survey.microservice.domain.data.SurveyRulesDTO;
import com.survey.microservice.domain.data.UserComment;
import com.survey.microservice.domain.ports.api.ICommentService;
import com.survey.microservice.domain.ports.api.IProjectService;
import com.survey.microservice.domain.ports.api.ISurveyService;
import com.survey.microservice.domain.ports.api.ITimestampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MicroServiceController {

    private final IProjectService projectService;
    private final ISurveyService surveyService;
    private final ICommentService commentService;
    private final ITimestampService timestampService;
    private final CookieHelper cookieHelper;

    @Autowired
    public MicroServiceController(IProjectService projectService,
                                ISurveyService surveyService,
                                  ITimestampService tsService,
                                  ICommentService commentService,
                                  CookieHelper cookieHelper) {
        this.projectService = projectService;
        this.surveyService = surveyService;
        this.commentService = commentService;
        this.timestampService = tsService;
        this.cookieHelper = cookieHelper;
    }

    @GetMapping("/projects")
    @ResponseBody
    public ResponseEntity<List<ProjectDTO>> getProjects(HttpServletRequest request,
                                            HttpServletResponse response) {

        List<ProjectDTO> projects = projectService.getProjects();

        if (!projects.isEmpty()) {
            Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
            String timeStampEncrypted = timestampService.encryptTimeStamp(timeStamp);
            response.addHeader("Set-Cookie", "timestamp=" + timeStampEncrypted + ";HttpOnly; SameSite=None; Secure; Path=/");

        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/rules")
    @ResponseBody
    public ResponseEntity<Boolean> getRules(@RequestParam String featureUrl,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {

        Optional<SurveyRulesDTO> surveyRules = surveyService.getSurveyRules(featureUrl);

        Optional<Cookie> cookieUserId = cookieHelper.getUserId(request.getCookies());

        Optional<LocalDateTime> lastTimeUserAnswered;
        String userId;
        if (cookieUserId.isPresent()) {
            userId = cookieUserId.get().getValue();
            lastTimeUserAnswered = surveyService.findLastTimeUserAnswered(userId, featureUrl);
        } else {
            lastTimeUserAnswered = Optional.empty();
            userId = surveyService.generateUserId();
        }
        response.addHeader("Set-Cookie", "userId=" + userId + ";HttpOnly; SameSite=None; Secure; Path=/");
        boolean result = surveyService.checkSurveyRules(surveyRules, lastTimeUserAnswered);

        if (result) {
            Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
            String timeStampEncrypted = timestampService.encryptTimeStamp(timeStamp);
            response.addHeader("Set-Cookie", "timestamp=" + timeStampEncrypted + ";HttpOnly; SameSite=None; Secure; Path=/");

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> addComment(@RequestBody UserComment comment,
                                           HttpServletRequest request) {

        Optional<Cookie> cookieUserId = cookieHelper.getUserId(request.getCookies());

        if (cookieUserId.isPresent()) {
            Optional<Cookie> cookieTimestamp = cookieHelper.getTimestamp(request.getCookies());

            Optional<SurveyRulesDTO> surveyRules = surveyService.getSurveyRules(comment.getFeatureUrl());

            Utils.allOf(cookieTimestamp, surveyRules, (cookie, rules) -> {
                Timestamp ts = timestampService.decryptTimeStamp(cookie.getValue());
                LocalDateTime time = ts.toLocalDateTime();
                boolean result = surveyService.checkDelayToAnswer(rules, time);
                if (result) {
                    commentService.sendComment(comment, cookieUserId.get().getValue());
                }
            });
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
