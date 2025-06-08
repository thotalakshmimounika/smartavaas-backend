package com.smartavaas.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MaintenanceResponseDto {

    private Long Userid;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;

}
