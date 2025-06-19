// AccountSearchDTO.java
package com.eewms.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountSearchDTO {
    private String fullName;
    private Boolean isBlock;
}
