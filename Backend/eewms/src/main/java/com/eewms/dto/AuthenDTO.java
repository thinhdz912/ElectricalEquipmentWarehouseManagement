package com.eewms.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class AuthenDTO {
    private String username;
    private String password;
}
