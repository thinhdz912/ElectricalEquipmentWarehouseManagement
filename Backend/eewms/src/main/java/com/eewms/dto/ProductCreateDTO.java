package com.eewms.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDTO {
    private String code;
    private String name;

    private BigDecimal originPrice;
    private BigDecimal listingPrice;

    private String description;
    private String status;

    private Integer unitId;
    private Integer categoryId;
    private Integer brandId;

    private List<String> images; // List<String> image URLs
}