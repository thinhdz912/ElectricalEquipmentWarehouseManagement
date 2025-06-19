// AccountUpdateDTO.java
package com.eewms.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountUpdateDTO {
    private String fullName;
    private String mobile;
    private Integer roleId;
}
