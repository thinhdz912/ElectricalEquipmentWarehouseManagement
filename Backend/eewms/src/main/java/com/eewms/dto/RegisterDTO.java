package com.eewms.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private Integer roleId;
}
