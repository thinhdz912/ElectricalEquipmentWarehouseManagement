package com.eewms.controller;

import com.eewms.constant.SettingType;
import com.eewms.dto.SettingDTO;
import com.eewms.exception.InventoryException;
import com.eewms.services.ISettingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {
    private final ISettingServices settingService;

    // Tạo mới → POST /api/settings
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SettingDTO dto) {
        try {
            SettingDTO created = settingService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Cập nhật → PUT /api/settings/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody SettingDTO dto) {
        try {
            SettingDTO updated = settingService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Xóa → DELETE /api/settings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            settingService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Lấy theo ID → GET /api/settings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(settingService.getById(id));
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Lấy tất cả → GET /api/settings
    @GetMapping
    public ResponseEntity<List<SettingDTO>> getAll() {
        return ResponseEntity.ok(settingService.getAll());
    }

    // Lấy theo type → GET /api/settings/type/{type}
    @GetMapping("/type/{type}")
    public ResponseEntity<List<SettingDTO>> getByType(@PathVariable SettingType type) {
        return ResponseEntity.ok(settingService.getByType(type));
    }
}
