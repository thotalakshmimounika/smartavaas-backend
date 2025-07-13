package com.smartavaas.dto;

import com.smartavaas.constants.AmenityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmenityRequestDTO {
    private String name;
    private String description;
}
