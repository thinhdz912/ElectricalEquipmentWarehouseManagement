package com.eewms.controller;

import com.eewms.dto.SettingDTO;
import com.eewms.exception.InventoryException;
import com.eewms.services.ISettingServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {

    private final ISettingServicesImpl settingService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody SettingDTO dto) {
        try {
            return new ResponseEntity<>(
                    settingService.create(dto),
                    HttpStatus.OK
            );
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody SettingDTO dto) {
        try {
            return new ResponseEntity<>(
                    settingService.update(id, dto),
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
            settingService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Xoá thành công"));
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(
                    settingService.getById(id),
                    HttpStatus.OK
            );
        } catch (InventoryException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<List<SettingDTO>> getAll() {
        List<SettingDTO> list = settingService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<SettingDTO>> getByTypeId(@PathVariable Integer typeId) {
        List<SettingDTO> list = settingService.getByTypeId(typeId);
        return ResponseEntity.ok(list);
    }

}
