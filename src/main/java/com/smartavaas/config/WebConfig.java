package com.smartavaas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//// My local change,,,,git pullgit add
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://develop--adorable-liger-d685f6.netlify.app", "http://localhost:4200", "http://frontend-219121912.us-east-1.elb.amazonaws.com", "http://3.231.160.198","https://dev.resiassist.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

