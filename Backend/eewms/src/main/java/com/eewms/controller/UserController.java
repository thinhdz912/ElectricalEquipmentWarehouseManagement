package com.eewms.controller;

import com.eewms.dto.UserDTO;
import com.eewms.dto.UserMapper;
import com.eewms.entities.User;
import com.eewms.repository.RoleRepository;
import com.eewms.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final RoleRepository roleRepository;

    // 1. Danh sách người dùng
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

    // 2. Hiển thị form tạo
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEnabled(true);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("allRoles", userService.getAllRoles());

        return "user-form";
    }

    // 3. Xử lý tạo
    @PostMapping
    public String createUser(@ModelAttribute("userDTO") UserDTO userDTO,
                             RedirectAttributes redirect) {
        try {
            User user = UserMapper.toEntity(userDTO, roleRepository);
            userService.saveUser(user);
            redirect.addFlashAttribute("message", "Tạo người dùng thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // 4. Hiển thị form sửa
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

    // 5. Xử lý cập nhật
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("userDTO") UserDTO userDTO,
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

    // 6. Xử lý xóa
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            userService.deleteUser(id);
            redirect.addFlashAttribute("message", "Xóa người dùng thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    // 7. Bật / Tắt trạng thái
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            userService.toggleEnabledStatus(id);
            redirect.addFlashAttribute("message", "Cập nhật trạng thái thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

//    // 8. Trang hồ sơ user hiện tại
//    @GetMapping("/profile")
//    public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
//        User user = userService.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));
//
//        UserDTO userDTO = UserMapper.toDTO(user);
//
//        //Tạo chuỗi vai trò ngăn cách bằng dấu phẩy
//        String joinedRoles = String.join(", ", userDTO.getRoleNames());
//
//        model.addAttribute("userDTO", userDTO);
//        model.addAttribute("joinedRoles", joinedRoles);
//
//        return "profile";
//    }
//
//    // 9. Cập nhật hồ sơ user hiện tại
//    @PostMapping("/update-profile")
//    public String updateProfile(@ModelAttribute("userDTO") UserDTO userDTO,
//                                RedirectAttributes redirect,
//                                @AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            User existingUser = userService.findByUsername(userDetails.getUsername())
//                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));
//
//            existingUser.setFullName(userDTO.getFullName());
//            existingUser.setEmail(userDTO.getEmail());
//            existingUser.setPhone(userDTO.getPhone());
//            existingUser.setAddress(userDTO.getAddress());
//
//            userService.updateUser(existingUser.getId(), existingUser);
//            redirect.addFlashAttribute("message", "Cập nhật hồ sơ thành công.");
//        } catch (Exception e) {
//            redirect.addFlashAttribute("error", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
//        }
//
//        return "redirect:/admin/users/profile";
//    }
}
