package com.eewms.services.impl;

import com.eewms.dto.CustomerCreateDTO;
import com.eewms.dto.CustomerDetailsDTO;
import com.eewms.dto.CustomerUpdateDTO;
import com.eewms.entities.Customer;
import com.eewms.exception.InventoryException;
import com.eewms.repository.CustomerRepository;
import com.eewms.services.ICustomerServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerServices {

    private final CustomerRepository customerRepo;
    private final ModelMapper modelMapper;

    @Override
    public CustomerDetailsDTO getById(Integer id) {
        return null;
    }

    @Override
    public List<CustomerDetailsDTO> getAll() {
        return List.of();
    }

    @Override
    public CustomerDetailsDTO create(CustomerCreateDTO dto) {
        // Map DTO -> Entity
        Customer customer = modelMapper.map(dto, Customer.class);

        // Save
        Customer saved = customerRepo.save(customer);

        // Return DTO
        return modelMapper.map(saved, CustomerDetailsDTO.class);
    }

    @Override
    public CustomerDetailsDTO update(Integer id, CustomerUpdateDTO dto) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    // Các method khác sẽ làm sau (update, delete, getById, getAll)
}