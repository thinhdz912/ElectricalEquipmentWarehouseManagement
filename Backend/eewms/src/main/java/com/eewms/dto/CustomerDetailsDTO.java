package com.eewms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetailsDTO {
    private Integer id;
    private String fullName;
    private String storeName;
    private String mobile;
    private String email;
    private String address;
    private String taxCode;
    private Boolean status;
    private String note;
}
