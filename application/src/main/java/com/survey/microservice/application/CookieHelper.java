package com.survey.microservice.application;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;

@Component
public class CookieHelper {

    Optional<Cookie> getUserId(Cookie[] cookies){
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "userId".equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }

    Optional<Cookie> getTimestamp(Cookie[] cookies){
        return Arrays.stream(cookies)
                .filter(cookie -> "timestamp".equals(cookie.getName()))
                .findFirst();
    }
}
