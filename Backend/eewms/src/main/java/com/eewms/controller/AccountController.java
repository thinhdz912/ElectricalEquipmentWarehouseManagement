package com.eewms.controller;

import com.eewms.dto.UserDTO;
import com.eewms.dto.UserMapper;
import com.eewms.entities.User;
import com.eewms.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {


    private final IUserService userService;

    // Hiển thị hồ sơ cá nhân
    @GetMapping("/info")
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        UserDTO userDTO = UserMapper.toDTO(user);
        model.addAttribute("userDTO", userDTO);
        return "profile";

    }

    // Cập nhật hồ sơ cá nhân
    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("userDTO") UserDTO userDTO,
                                RedirectAttributes redirect,
                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User existingUser = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

            // Chỉ cập nhật các trường cho phép người dùng thay đổi
            existingUser.setFullName(userDTO.getFullName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setPhone(userDTO.getPhone());
            existingUser.setAddress(userDTO.getAddress());

            userService.updateUser(existingUser.getId(), existingUser);

            redirect.addFlashAttribute("message", "Cập nhật hồ sơ thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
        }

        return "redirect:/account/info";
    }
    // 1. Hiển thị form đổi mật khẩu
    @GetMapping("/change-password")
    public String showChangePasswordForm() {
        return "change-password"; // Trỏ đến file templates/change-password.html
    }

    // 2. Xử lý đổi mật khẩu
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirect) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                redirect.addFlashAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
                return "redirect:/account/change-password";
            }

            userService.changePassword(userDetails.getUsername(), oldPassword, newPassword);
            redirect.addFlashAttribute("message", "Đổi mật khẩu thành công.");
            return "redirect:/account/info";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/account/change-password";
        }
    }

}
