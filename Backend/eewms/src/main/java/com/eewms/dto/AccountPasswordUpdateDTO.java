// AccountPasswordUpdateDTO.java
package com.eewms.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountPasswordUpdateDTO {
    private String oldPassword;
    private String newPassword;
}
