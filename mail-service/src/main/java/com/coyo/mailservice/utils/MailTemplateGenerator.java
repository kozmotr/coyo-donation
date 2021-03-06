package com.coyo.mailservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MailTemplateGenerator {

    private final SpringTemplateEngine templateEngine;

    public String createTemplate(String templateName, Locale locale, Map<String, Object> args) {
        Map<String, Object> variables = args;
        if (args == null) {
            variables = new HashMap<>();
        }
        return templateEngine.process(templateName, new Context(locale, variables));
    }
}
