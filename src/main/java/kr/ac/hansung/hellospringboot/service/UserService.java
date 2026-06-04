package kr.ac.hansung.hellospringboot.service;

import kr.ac.hansung.hellospringboot.dto.PasswordChangeDto;
import kr.ac.hansung.hellospringboot.model.User;
import kr.ac.hansung.hellospringboot.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원 비밀번호 변경
     * @param username 현재 로그인된 사용자명
     * @param dto 비밀번호 변경 정보 DTO
     */
    @Transactional
    public void changePassword(String username, PasswordChangeDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }

        // 새 비밀번호 인코딩 후 저장
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        // Dirty checking에 의해 트랜잭션 완료 시 자동 UPDATE 쿼리 반영
    }
}
