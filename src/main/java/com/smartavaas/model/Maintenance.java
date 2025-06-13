package com.smartavaas.model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    public Maintenance() {

    }

    public void setAssignedAgent(String name) {

    }
}

