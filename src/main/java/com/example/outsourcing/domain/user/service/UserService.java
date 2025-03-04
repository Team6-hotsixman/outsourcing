package com.example.outsourcing.domain.user.service;

import com.example.outsourcing.domain.auth.config.PasswordEncoder;
import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDto getUser(long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return new UserResponseDto(user);
    }

    public UserResponseDto passwordUpdate(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_PASSWORD);
        }

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.MISS_MATCH_PASSWORD);
        }

        user.passwordUpdate(passwordEncoder.encode(requestDto.getNewPassword()));

        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId, UserDeleteRequestDto requestDto) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if (user.getUserStatus() == UserStatus.DELETE) {
            throw new ApplicationException(ErrorCode.USER_STATUS_DELETE);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.MISS_MATCH_PASSWORD);
        }

        UserStatus userStatus = UserStatus.DELETE;
        user.UpdateUserStatus(userStatus);
    }
}
