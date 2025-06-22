package com.eewms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;

    private String storeName;

    private String mobile;

    private String email;

    private String address;

    private String taxCode;

    private Boolean status;

    @Column(columnDefinition = "TEXT")
    private String note;
}