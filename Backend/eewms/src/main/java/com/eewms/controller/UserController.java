package com.eewms.controller;

import com.eewms.dto.UserDTO;
import com.eewms.dto.UserMapper;
import com.eewms.entities.User;
import com.eewms.repository.RoleRepository;
import com.eewms.services.IUserService;
import com.eewms.services.IEmailService;
import com.eewms.services.IVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final RoleRepository roleRepository;
    private final IVerificationTokenService verificationTokenService;
    private final IEmailService emailService;

    // 1. Danh sách người dùng
    @GetMapping
    public String listUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        System.out.println("🔍 Từ khóa tìm kiếm: " + keyword); // THÊM DÒNG NÀY

        Page<UserDTO> userPage = userService.searchUsers(page, keyword);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("userPage", userPage);
        model.addAttribute("keyword", keyword); // giữ lại từ khóa tìm kiếm

        // ✅ Thêm dòng này:
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("allRoles", userService.getAllRoles());

        return "user-list";
    }


    // 2. Hiển thị form tạo (không dùng nếu dùng modal)
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEnabled(false);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("allRoles", userService.getAllRoles());

        return "user-form";
    }

    // 3. Xử lý tạo user và gửi mail kích hoạt
    @PostMapping
    public String createUser(@ModelAttribute("userDTO") UserDTO userDTO,
                             RedirectAttributes redirect) {
        try {
            // Bắt buộc trạng thái ban đầu là chưa kích hoạt
            userDTO.setEnabled(false);

            // Convert DTO → Entity
            User user = UserMapper.toEntity(userDTO, roleRepository);

            // Lưu user (không có password)
            userService.saveUser(user);

            // Tạo token kích hoạt
            String token = verificationTokenService.createVerificationToken(user);

            // Gửi email xác thực
            emailService.sendActivationEmail(user, token);

            redirect.addFlashAttribute("message", "Tạo người dùng thành công. Đã gửi email kích hoạt.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi tạo người dùng: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // 7. Bật / Tắt trạng thái
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            User user = userService.findUserById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

            // Nếu đang tắt → chuẩn bị bật
            if (!user.isEnabled()) {
                if (user.getPassword() == null || user.getPassword().isBlank()) {
                    redirect.addFlashAttribute("error", "Không thể bật tài khoản vì người dùng chưa kích hoạt qua email.");
                    return "redirect:/admin/users";
                }
            }

            // Thực hiện đảo trạng thái
            userService.toggleEnabledStatus(id);
            redirect.addFlashAttribute("message", "Cập nhật trạng thái thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}
