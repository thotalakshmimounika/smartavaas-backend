package com.smartavaas.service;

import com.smartavaas.enums.EmailTemplateKey;
import com.smartavaas.model.EmailTemplate;
import com.smartavaas.repository.EmailTemplateRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateRepository templateRepository;

    @Autowired
    private TemplateProcessorService processorService;

    //private static final Logger logger = (Logger) LoggerFactory.getLogger(EmailService.class);


    @Value("${spring.mail.username}")
    private String emailResident;

    public void setMail(String toEmail , EmailTemplateKey templateKey , Map<String,Object> data) {
        try{
            EmailTemplate template = templateRepository.findByTemplateKeyAndActiveTrue(templateKey)
                    .orElseThrow(() -> new IllegalArgumentException("Template not found"));
            String subject = processorService.process(template.getSubject(),data);
            String body = processorService.process(template.getBody(),data);

            //SimpleMailMessage message = new SimpleMailMessage();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true); // true = multipart
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body,true);
            helper.setFrom(emailResident);
            mailSender.send(message);

        }catch (MessagingException e) {
            System.err.println("Email sending failed: " + e.getMessage());
            e.printStackTrace(); // log full stack trace
            throw new RuntimeException("Email sending failed", e);
        }
    }

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("SmartAvaas - Verify Your Email");
        message.setText("Welcome to SmartAvaas! Your verification code is: " + code);
        message.setFrom(emailResident);
        mailSender.send(message);
    }
}

