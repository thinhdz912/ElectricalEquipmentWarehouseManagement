package com.eewms.dto;

import com.eewms.constant.SettingType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingDTO {
    private Integer id;
    private String name;
    private SettingType type;
    private Integer priority;
    private String description;
    private String status;
}
