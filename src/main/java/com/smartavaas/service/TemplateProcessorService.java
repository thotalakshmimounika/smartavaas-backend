package com.smartavaas.service;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class TemplateProcessorService {

    private final TemplateEngine templateEngine;


    public TemplateProcessorService() {
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode("HTML"); // Use "TEXT" if using plain text
        resolver.setCacheable(false);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    public String process(String templateContent, Map<String,Object> variables){
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateContent,context);
    }
}
