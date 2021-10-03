package com.presto.auth.controller.advise;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
public class LanguageAdvice {

    private static final String ACCEPT_LANGUAGE = "Accept-Language";

    @ModelAttribute
    void language(HttpServletRequest request) {

        String lang = request.getHeader(ACCEPT_LANGUAGE);
    }
}
