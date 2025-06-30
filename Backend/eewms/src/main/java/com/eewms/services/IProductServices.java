package com.eewms.services;

import com.eewms.dto.ProductCreateDTO;
import com.eewms.dto.ProductDetailsDTO;
import com.eewms.dto.ProductUpdateDTO;
import com.eewms.exception.InventoryException;

import java.util.List;

public interface IProductServices {

    ProductDetailsDTO create(ProductCreateDTO dto) throws InventoryException;

    ProductDetailsDTO update(Integer id, ProductUpdateDTO dto) throws InventoryException;

    void delete(Integer id) throws InventoryException;

    ProductDetailsDTO getById(Integer id) throws InventoryException;

    List<ProductDetailsDTO> getAll();
}