package com.eewms.dto;

import com.eewms.entities.Role;
import com.eewms.entities.User;
import com.eewms.repository.RoleRepository;

import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    // DTO → Entity (tạo hoặc cập nhật user)
    public static User toEntity(UserDTO dto, RoleRepository roleRepository) {
        Set<Role> roles = dto.getRoleIds().stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword()) // sẽ mã hóa ở tầng service
                .fullName(dto.getFullName())
                .enabled(dto.isEnabled())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .roles(roles)
                .build();
    }

    // Entity → DTO (hiển thị form hoặc bảng)
    public static UserDTO toDTO(User user) {
        List<Long> roleIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList());

        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", "")) // dễ đọc hơn
                .collect(Collectors.toList());

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .enabled(user.isEnabled())
                .phone(user.getPhone())
                .email(user.getEmail())
                .address(user.getAddress())
                .roleIds(roleIds)
                .roleNames(roleNames)
                .build();
    }
}
