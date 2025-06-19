package com.eewms.controller;

import com.eewms.dto.*;
import com.eewms.exception.InventoryException;
import com.eewms.services.IAccountServices;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {
    private final IAccountServices accountServices;

    @GetMapping
    public ResponseEntity<Object> findAll(
            HttpServletRequest req,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String fullName,
            @RequestParam(required=false) Boolean isBlock
    ) {
        try {
            String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
            AccountSearchDTO dto = new AccountSearchDTO(fullName, isBlock);
            Pageable pg = PageRequest.of(page, size);
            return ResponseEntity.ok(
                    accountServices.getAll(auth, pg, dto)
            );
        } catch (InventoryException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("code", ex.getCode(), "message", ex.getMessage()));
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Object> findByUsername(
            @PathVariable String username,
            HttpServletRequest req
    ) {
        try {
            String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
            return ResponseEntity.ok(
                    accountServices.findByUsername(auth, username)
            );
        } catch (InventoryException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("code", ex.getCode(), "message", ex.getMessage()));
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> updateByUsername(
            @PathVariable String username,
            HttpServletRequest req,
            @RequestBody AccountUpdateDTO dto
    ) {
        try {
            accountServices.updateByUsername(
                    req.getHeader(HttpHeaders.AUTHORIZATION),
                    username,
                    dto
            );
            return ResponseEntity.accepted().build();
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{username}/lock")
    public ResponseEntity<Void> lock(
            @PathVariable String username,
            HttpServletRequest req
    ) {
        try {
            accountServices.lockAccount(
                    req.getHeader(HttpHeaders.AUTHORIZATION),
                    username
            );
            return ResponseEntity.accepted().build();
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{username}/unlock")
    public ResponseEntity<Void> unlock(
            @PathVariable String username,
            HttpServletRequest req
    ) {
        try {
            accountServices.unlockAccount(
                    req.getHeader(HttpHeaders.AUTHORIZATION),
                    username
            );
            return ResponseEntity.accepted().build();
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-password/{username}")
    public ResponseEntity<Void> updatePassword(
            @PathVariable String username,
            HttpServletRequest req,
            @RequestBody AccountPasswordUpdateDTO dto
    ) {
        try {
            accountServices.updatePasswordForAccount(
                    req.getHeader(HttpHeaders.AUTHORIZATION),
                    username,
                    dto
            );
            return ResponseEntity.accepted().build();
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
