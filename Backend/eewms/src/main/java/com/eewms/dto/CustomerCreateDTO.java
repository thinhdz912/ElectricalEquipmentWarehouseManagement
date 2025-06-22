package com.eewms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreateDTO {
    private String fullName;
    private String storeName;
    private String mobile;
    private String email;
    private String address;
    private String taxCode;
    private String note;
}
