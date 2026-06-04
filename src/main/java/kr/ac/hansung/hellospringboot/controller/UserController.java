package kr.ac.hansung.hellospringboot.controller;

import kr.ac.hansung.hellospringboot.dto.PasswordChangeDto;
import kr.ac.hansung.hellospringboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 비밀번호 변경 화면 호출
     */
    @GetMapping("/user/password")
    public String passwordChangeForm(Model model) {
        model.addAttribute("passwordDto", new PasswordChangeDto());
        return "user/password";
    }

    /**
     * 비밀번호 변경 요청 처리
     */
    @PostMapping("/user/password")
    public String changePassword(
            @Valid @ModelAttribute("passwordDto") PasswordChangeDto dto,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        // 1. 기본 필드 검증 (NotBlank, Size 등)
        if (bindingResult.hasErrors()) {
            return "user/password";
        }

        // 2. 새 비밀번호와 비밀번호 확인 일치 확인
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "New password and confirmation password do not match.");
            return "user/password";
        }

        // 3. 현재 비밀번호 검증 및 변경 로직 호출
        try {
            userService.changePassword(principal.getName(), dto);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("currentPassword", "error.currentPassword", e.getMessage());
            return "user/password";
        }

        // 4. 성공 시 알림 속성 및 리다이렉트
        redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        return "redirect:/products";
    }
}
