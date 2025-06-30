package com.eewms.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailsDTO {
    private Integer id;
    private String code;
    private String name;

    private BigDecimal originPrice;
    private BigDecimal listingPrice;

    private String description;
    private String status;

    private String unitName;
    private String categoryName;
    private String brandName;

    private List<ImageDTO> images;
}
