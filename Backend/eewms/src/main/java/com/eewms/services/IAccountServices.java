package com.eewms.services;

import com.eewms.dto.*;
import com.eewms.entities.Account;
import com.eewms.exception.InventoryException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAccountServices {
    Page<Account> getAll(String authHeader, Pageable pageable, AccountSearchDTO searchDTO)
            throws InventoryException;

    Account getFullInformation(String authHeader);

    Account findByUsername(String authHeader, String username)
            throws InventoryException;

    void updateByUsername(String authHeader, String username, AccountUpdateDTO dto)
            throws InventoryException;

    void lockAccount(String authHeader, String username)
            throws InventoryException;

    void unlockAccount(String authHeader, String username)
            throws InventoryException;

    void updatePasswordForAccount(String authHeader, String username, AccountPasswordUpdateDTO dto)
            throws InventoryException;
}
