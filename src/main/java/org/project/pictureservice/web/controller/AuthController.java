package org.project.pictureservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.project.pictureservice.domain.user.User;
import org.project.pictureservice.service.AuthService;
import org.project.pictureservice.service.UserService;
import org.project.pictureservice.web.dto.auth.JwtRequest;
import org.project.pictureservice.web.dto.auth.JwtResponse;
import org.project.pictureservice.web.dto.user.UserDto;
import org.project.pictureservice.web.dto.validation.OnCreate;
import org.project.pictureservice.web.mappers.UserMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated
                             @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class)
                            @RequestBody final UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody final String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
