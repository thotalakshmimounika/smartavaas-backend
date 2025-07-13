package com.smartavaas.model;

import java.time.LocalDateTime;

import com.smartavaas.constants.AmenityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "amenities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AmenityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}