package com.eewms.dto;

import com.eewms.constant.SettingType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingDTO {
    private Integer id;
    private String name;
    private SettingType type;      // ← đổi sang enum
    private Integer priority;
    private String description;
    private String status;
}
