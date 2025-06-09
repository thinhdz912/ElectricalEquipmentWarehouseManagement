package com.eewms.services;

import com.eewms.dto.AuthenDTO;
import com.eewms.dto.RegisterDTO;
import com.eewms.dto.response.AuthenResponseDTO;
import com.eewms.exception.InventoryException;

public interface IAuthenticatedServices {
    AuthenResponseDTO login(AuthenDTO authenDTO) throws InventoryException;
    void register(RegisterDTO registerDTO) throws InventoryException;
}
