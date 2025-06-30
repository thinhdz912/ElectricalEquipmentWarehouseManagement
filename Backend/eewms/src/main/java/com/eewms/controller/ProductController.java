package com.eewms.controller;

import com.eewms.dto.ProductCreateDTO;
import com.eewms.dto.ProductDetailsDTO;
import com.eewms.dto.ProductUpdateDTO;
import com.eewms.exception.InventoryException;
import com.eewms.services.IProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductServices productService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductCreateDTO dto) {
        try {
            return new ResponseEntity<>(
                    productService.create(dto),
                    HttpStatus.OK
            );
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody ProductUpdateDTO dto) {
        try {
            return new ResponseEntity<>(
                    productService.update(id, dto),
                    HttpStatus.OK
            );
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Xoá sản phẩm thành công"));
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(
                    productService.getById(id),
                    HttpStatus.OK
            );
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDetailsDTO>> getAll() {
        List<ProductDetailsDTO> list = productService.getAll();
        return ResponseEntity.ok(list);
    }
}
