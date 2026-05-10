package com.openjournal.backend.service;

import com.openjournal.backend.dto.request.LoginRequest;
import com.openjournal.backend.dto.request.RegisterRequest;
import com.openjournal.backend.dto.response.AuthResponse;
import com.openjournal.backend.dto.response.UserDto;
import com.openjournal.backend.exception.DuplicateResourceException;
import com.openjournal.backend.exception.ResourceNotFoundException;
import com.openjournal.backend.exception.UnauthorizedException;
import com.openjournal.backend.model.User;
import com.openjournal.backend.repository.UserRepository;
import com.openjournal.backend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email is already taken");
        }
        if (userRepository.existsByUsername(request.name())) {
            throw new DuplicateResourceException("Username is already taken");
        }

        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.avatarUrl()
        );

        userRepository.save(user);

        String jwtToken = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());

        return new AuthResponse(
                jwtToken,
                mapToDto(user)
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Attempting login for email: {}", request.email());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseGet(() -> userRepository.findByUsername(request.email())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        String jwtToken = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());

        return new AuthResponse(
                jwtToken,
                mapToDto(user)
        );
    }

    @Override
    public UserDto getMe(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return mapToDto(user);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getAvatarUrl(),
                user.getCreatedAt()
        );
    }
}
