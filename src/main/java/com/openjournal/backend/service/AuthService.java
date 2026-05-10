package com.openjournal.backend.service;

import com.openjournal.backend.dto.request.LoginRequest;
import com.openjournal.backend.dto.request.RegisterRequest;
import com.openjournal.backend.dto.response.AuthResponse;
import com.openjournal.backend.dto.response.UserDto;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserDto getMe(String username);
}
