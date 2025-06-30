package com.eewms.services;

import com.eewms.dto.SettingDTO;
import com.eewms.exception.InventoryException;

import java.util.List;

public interface ISettingServicesImpl {
    SettingDTO create(SettingDTO dto) throws InventoryException;

    SettingDTO update(Integer id, SettingDTO dto) throws InventoryException;

    void delete(Integer id) throws InventoryException;

    SettingDTO getById(Integer id) throws InventoryException;

    List<SettingDTO> getAll();

    List<SettingDTO> getByTypeId(Integer typeId);
}
