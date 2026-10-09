package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.*;
import com.prachi18.college_management_system.Entities.RefreshToken;
import com.prachi18.college_management_system.Entities.User;
import com.prachi18.college_management_system.Enums.Role;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.RefreshTokenRepository;
import com.prachi18.college_management_system.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginTokens login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );
        User user= userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);
        String refreshTokenHash= jwtService.hashRefreshToken(refreshToken);
        RefreshToken refreshTokenEntity= RefreshToken.builder()
                .tokenHash(refreshTokenHash)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMonths(6))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return new LoginTokens(accessToken, refreshToken);
    }

    public SignUpResponseDto signUp(SignUpRequestDto request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }
        User toBeCreatedUser= modelMapper.map(request,User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(request.getPassword()));
        //public signup will always create account as a STUDENT
        toBeCreatedUser.setRole(Role.STUDENT);
        User savedUser= userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser,SignUpResponseDto.class);
    }

    public void logOut(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }

        String tokenHash = jwtService.hashRefreshToken(refreshToken);

        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    if (!token.isRevoked()) {
                        token.setRevoked(true);
                        refreshTokenRepository.save(token);
                    }
                });
    }

}
