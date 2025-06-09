package com.eewms.dto.response;

import com.eewms.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthenResponseDTO {
    private String token;
    private Account info;
}
