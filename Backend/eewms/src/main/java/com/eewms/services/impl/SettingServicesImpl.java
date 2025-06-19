package com.eewms.services.impl;

import com.eewms.dto.SettingDTO;
import com.eewms.entities.Setting;
import com.eewms.exception.InventoryException;
import com.eewms.repository.SettingRepository;
import com.eewms.services.ISettingServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SettingServicesImpl implements ISettingServicesImpl {

    private final SettingRepository settingRepository;

    @Override
    public SettingDTO create(SettingDTO dto) throws InventoryException {
        // Kiểm tra name đã tồn tại trong typeId chưa
        if (settingRepository.existsByNameAndTypeId(dto.getName(), dto.getTypeId())) {
            throw new InventoryException("Tên đã tồn tại trong nhóm này");
        }

        // Tạo entity từ DTO
        Setting setting = Setting.builder()
                .name(dto.getName())
                .typeId(dto.getTypeId())
                .priority(dto.getPriority())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();

        // Lưu vào DB
        Setting saved = settingRepository.save(setting);

        // Trả về DTO sau khi lưu
        return SettingDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .typeId(saved.getTypeId())
                .priority(saved.getPriority())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .build();
    }

    @Override
    public SettingDTO update(Integer id, SettingDTO dto) throws InventoryException {
        // Tìm setting theo ID, nếu không có thì quăng lỗi
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Không tìm thấy setting với ID: " + id));

        // Nếu tên mới khác tên cũ thì mới check trùng
        if (!setting.getName().equals(dto.getName())) {
            boolean exists = settingRepository.existsByNameAndTypeId(dto.getName(), dto.getTypeId());
            if (exists) {
                throw new InventoryException("Tên setting đã tồn tại trong nhóm này.");
            }
        }

        // Cập nhật thông tin
        setting.setName(dto.getName());
        setting.setTypeId(dto.getTypeId());
        setting.setPriority(dto.getPriority());
        setting.setDescription(dto.getDescription());
        setting.setStatus(dto.getStatus());

        // Lưu lại
        Setting updated = settingRepository.save(setting);

        // Trả về DTO
        return SettingDTO.builder()
                .id(updated.getId())
                .name(updated.getName())
                .typeId(updated.getTypeId())
                .priority(updated.getPriority())
                .description(updated.getDescription())
                .status(updated.getStatus())
                .build();
    }

    @Override
    public void delete(Integer id) throws InventoryException {
        // Kiểm tra xem setting có tồn tại không
        if (!settingRepository.existsById(id)) {
            throw new InventoryException("Không tìm thấy setting với ID: " + id);
        }

        // Xoá khỏi DB
        settingRepository.deleteById(id);
    }

    @Override
    public SettingDTO getById(Integer id) throws InventoryException {
        // Tìm setting theo ID, nếu không có thì ném lỗi
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Không tìm thấy setting với ID: " + id));

        // Trả về DTO
        return SettingDTO.builder()
                .id(setting.getId())
                .name(setting.getName())
                .typeId(setting.getTypeId())
                .priority(setting.getPriority())
                .description(setting.getDescription())
                .status(setting.getStatus())
                .build();
    }

    @Override
    public List<SettingDTO> getAll() {
        // Lấy toàn bộ entity từ DB
        List<Setting> list = settingRepository.findAll();

        // Convert sang DTO
        return list.stream().map(setting ->
                SettingDTO.builder()
                        .id(setting.getId())
                        .name(setting.getName())
                        .typeId(setting.getTypeId())
                        .priority(setting.getPriority())
                        .description(setting.getDescription())
                        .status(setting.getStatus())
                        .build()
        ).toList();
    }

    @Override
    public List<SettingDTO> getByTypeId(Integer typeId) {
        // Lấy danh sách setting theo typeId
        List<Setting> list = settingRepository.findByTypeId(typeId);

        // Convert entity → DTO
        return list.stream().map(setting ->
                SettingDTO.builder()
                        .id(setting.getId())
                        .name(setting.getName())
                        .typeId(setting.getTypeId())
                        .priority(setting.getPriority())
                        .description(setting.getDescription())
                        .status(setting.getStatus())
                        .build()
        ).toList();
    }
}
