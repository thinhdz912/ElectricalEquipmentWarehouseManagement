package com.eewms.repository;

import com.eewms.entities.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {
    //	Check trùng tên trong cùng một loại setting
    boolean existsByNameAndTypeId(String name, Integer typeId);
    //	Lấy danh sách setting theo loại (Brand, Category, Unit...)
    List<Setting> findByTypeId(Integer typeId);
}