package com.eewms.services;



import com.eewms.dto.CustomerCreateDTO;
import com.eewms.dto.CustomerDetailsDTO;
import com.eewms.dto.CustomerUpdateDTO;
import com.eewms.exception.InventoryException;

import java.util.List;

public interface ICustomerServices {

    CustomerDetailsDTO getById(Integer id);

    List<CustomerDetailsDTO> getAll();

    CustomerDetailsDTO create(CustomerCreateDTO dto);

    CustomerDetailsDTO update(Integer id, CustomerUpdateDTO dto);

    void delete(Integer id);
}

