package com.eewms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingDTO {

    private Integer id;

    private String name;

    private Integer typeId; // Dùng enum SettingType.getValue()

    private Integer priority;

    private String description;

    private String status;
}
