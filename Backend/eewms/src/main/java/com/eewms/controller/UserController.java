package com.eewms.controller;

import com.eewms.dto.UserDTO;
import com.eewms.dto.UserMapper;
import com.eewms.entities.User;
import com.eewms.repository.RoleRepository;
import com.eewms.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final RoleRepository roleRepository;

    // Danh sách người dùng
    @GetMapping
    public String listUsers(Model model) {
        List<UserDTO> users = userService.findAllUsers()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "user-list";
    }

    //  Hiển thị form tạo user
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEnabled(true);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("allRoles", userService.getAllRoles());

        return "user-form";
    }

    // Xử lý tạo mới
    @PostMapping
    public String createUser(UserDTO userDTO, RedirectAttributes redirect) {
        try {
            User user = UserMapper.toEntity(userDTO, roleRepository);
            userService.saveUser(user);
            redirect.addFlashAttribute("message", "Tạo người dùng mới thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            User user = userService.findUserById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

            UserDTO userDTO = UserMapper.toDTO(user);

            model.addAttribute("userDTO", userDTO);
            model.addAttribute("allRoles", userService.getAllRoles());

            return "user-form";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }

    //  Xử lý cập nhật user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             UserDTO userDTO,
                             RedirectAttributes redirect) {
        try {
            User updatedUser = UserMapper.toEntity(userDTO, roleRepository);
            userService.updateUser(id, updatedUser);
            redirect.addFlashAttribute("message", "Cập nhật người dùng thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi cập nhật: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    //  Xử lý xóa user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes redirect) {
        try {
            userService.deleteUser(id);
            redirect.addFlashAttribute("message", "Xóa người dùng thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    // Bật / Tắt tài khoản
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Long id,
                               RedirectAttributes redirect) {
        try {
            userService.toggleEnabledStatus(id);
            redirect.addFlashAttribute("message", "Cập nhật trạng thái tài khoản thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi thay đổi trạng thái: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}
