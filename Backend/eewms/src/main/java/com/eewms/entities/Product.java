package com.eewms.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String name;

    @Column(name = "origin_price")
    private BigDecimal originPrice;

    @Column(name = "listing_price")
    private BigDecimal listingPrice;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    @ManyToOne
    @JoinColumn(name = "unit_id")  // ← KHÔNG được thiếu dòng này
    private Setting unit;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Setting category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Setting brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}