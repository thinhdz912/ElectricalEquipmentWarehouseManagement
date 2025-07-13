package com.eewms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="images")
@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class Image {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable=false)
    private Product product;
}
