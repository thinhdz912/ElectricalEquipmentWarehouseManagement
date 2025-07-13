package com.eewms.services;

import com.eewms.constant.SettingType;
import com.eewms.dto.ProductFormDTO;
import com.eewms.dto.ProductDetailsDTO;
import com.eewms.dto.SettingDTO;
import com.eewms.exception.InventoryException;
import java.util.List;

public interface IProductServices {
    ProductDetailsDTO create(ProductFormDTO dto) throws InventoryException;
    ProductDetailsDTO update(Integer id, ProductFormDTO dto) throws InventoryException;
    void delete(Integer id) throws InventoryException;
    ProductDetailsDTO getById(Integer id) throws InventoryException;
    List<ProductDetailsDTO> getAll() throws InventoryException;

    // để lấy options trong dropdown
    List<SettingDTO> getSettingOptions(SettingType type);
}
