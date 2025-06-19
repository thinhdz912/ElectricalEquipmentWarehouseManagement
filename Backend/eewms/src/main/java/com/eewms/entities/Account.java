package com.eewms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String mobile;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "is_block")
    private Boolean isBlock;      // * bổ sung để lock/unlock tài khoản *
}
