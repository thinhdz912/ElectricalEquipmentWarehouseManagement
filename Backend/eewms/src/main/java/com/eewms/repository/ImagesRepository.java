package com.eewms.repository;

import com.eewms.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Integer> {

    // Lấy danh sách ảnh theo Product ID
    List<Image> findByProductId(Integer productId);

    // Optional: Xóa hết ảnh của 1 sản phẩm (nếu cần trong update)
    void deleteByProductId(Integer productId);
}
