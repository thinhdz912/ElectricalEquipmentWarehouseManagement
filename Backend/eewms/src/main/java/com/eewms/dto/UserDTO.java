package com.eewms.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private boolean enabled;

    private List<Long> roleIds;     // dùng để gửi id các role từ form
    private List<String> roleNames; // dùng để hiển thị tên role ra HTML
}
