package com.smartavaas.model;

import com.smartavaas.enums.EmailTemplateKey;
import com.smartavaas.repository.EmailTemplateRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

@Entity
@Table(name = "EMAIL_TEMPLATES")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EmailTemplate extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private EmailTemplateKey templateKey;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false , columnDefinition = "Text")
    private String body;
}
