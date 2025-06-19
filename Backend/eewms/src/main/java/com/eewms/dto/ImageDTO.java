package com.eewms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {
    private Integer id;
    private String imageUrl;
}
