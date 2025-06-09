package com.eewms.controller;

import com.eewms.dto.AuthenDTO;
import com.eewms.dto.RegisterDTO;
import com.eewms.dto.response.AuthenResponseDTO;
import com.eewms.exception.InventoryException;
import com.eewms.services.IAuthenticatedServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final IAuthenticatedServices authenticatedServices;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenDTO dto) {
        try {
            return new ResponseEntity<>(
                    authenticatedServices.login(dto),
                    HttpStatus.OK
            );
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        try {
            authenticatedServices.register(dto);
            return ResponseEntity.ok(Map.of("message", "Register successful"));
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}
