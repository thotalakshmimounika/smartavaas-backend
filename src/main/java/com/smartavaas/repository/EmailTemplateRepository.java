package com.smartavaas.repository;

import com.smartavaas.enums.EmailTemplateKey;
import com.smartavaas.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository < EmailTemplate,Long> {

    Optional<EmailTemplate> findByTemplateKeyAndActiveTrue(EmailTemplateKey templateKey);

}
