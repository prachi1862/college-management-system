package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Advices.ApiResponse;
import com.prachi18.college_management_system.DTO.*;
import com.prachi18.college_management_system.Entities.RefreshToken;
import com.prachi18.college_management_system.Entities.User;
import com.prachi18.college_management_system.Exceptions.InvalidRefreshTokenException;
import com.prachi18.college_management_system.Repositories.RefreshTokenRepository;
import com.prachi18.college_management_system.Repositories.UserRepository;
import com.prachi18.college_management_system.Services.AdminInvitationService;
import com.prachi18.college_management_system.Services.AuthService;
import com.prachi18.college_management_system.Services.JwtService;
import com.prachi18.college_management_system.Services.ProfessorInvitationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
     private  final AuthService authService;
     private final ProfessorInvitationService professorInvitationService;
     private final AdminInvitationService adminInvitationService;
     private final JwtService jwtService;
     private final UserRepository userRepository;
     private final RefreshTokenRepository refreshTokenRepository;

     @PostMapping("/login")
     public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginDto, HttpServletResponse response ){
          LoginTokens token= authService.login(loginDto);
          Cookie accessCookie= new Cookie("accessToken", token.getAccessToken());
          accessCookie.setHttpOnly(true);
          accessCookie.setPath("/");

          Cookie refreshCookie =new Cookie("refreshToken", token.getRefreshToken());
          refreshCookie.setHttpOnly(true);
          refreshCookie.setPath("/");

          response.addCookie(accessCookie);
          response.addCookie(refreshCookie);

          return new ApiResponse<>(new LoginResponseDTO("login successful"));
     }

     @PostMapping("/signup")
     public ApiResponse<SignUpResponseDto> signup(@RequestBody SignUpRequestDto request){
         return new ApiResponse<>(authService.signUp(request));
     }

     @PostMapping("/professor-signup")
     public ApiResponse<String> professorSignUp(@RequestBody ProfessorSignUpDTO request){
          professorInvitationService.professorSignUp(request);
          return new ApiResponse<>("Professor Sign Up Successful");
     }

    @PostMapping("/admin-signup")
    public ApiResponse<String> adminSignUp(@RequestBody AdminSignUpDTO request) {
        adminInvitationService.adminSignUp(request);
        return new ApiResponse<>("Admin Sign Up Successful");
    }

    @PostMapping("/refresh")
    public ApiResponse<String> refresh(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new InvalidRefreshTokenException("refreshToken not found in cookies");
        }

         String refreshToken= Arrays.stream(request.getCookies())
                 .filter(c -> c.getName().equals("refreshToken"))
                 .findFirst()
                 .map(Cookie::getValue)
                 .orElseThrow(() -> new RuntimeException("refreshToken not found in cookies"));

        String refreshTokenHash = jwtService.hashRefreshToken(refreshToken);

        RefreshToken storedToken = refreshTokenRepository
                .findByTokenHash(refreshTokenHash)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (storedToken.isRevoked()) {
            throw new InvalidRefreshTokenException("Refresh token has been revoked");
        }

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }
        Long userId= jwtService.getUserIdFromJwtToken(refreshToken);
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        User user= userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        String newRefreshToken = jwtService.generateRefreshToken(user);
        String newRefreshTokenHash = jwtService.hashRefreshToken(newRefreshToken);

        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .tokenHash(newRefreshTokenHash)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMonths(6))
                .revoked(false)
                .build();

        refreshTokenRepository.save(newRefreshTokenEntity);
        String accessToken= jwtService.generateAccessToken(user);
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", newRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");

        response.addCookie(refreshCookie);

        return new ApiResponse<>("Token refreshed successfully");
    }
}
