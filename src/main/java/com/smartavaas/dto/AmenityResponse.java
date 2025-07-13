package com.smartavaas.dto;

import com.smartavaas.constants.AmenityStatus;
import lombok.Data;

@Data
public class AmenityResponse {
    private Long id;
    private String name;
    private String description;
    private AmenityStatus status;
}