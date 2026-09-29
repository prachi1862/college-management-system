package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Advices.ApiResponse;
import com.prachi18.college_management_system.DTO.LoginRequestDTO;
import com.prachi18.college_management_system.DTO.ProfessorSignUpDTO;
import com.prachi18.college_management_system.DTO.SignUpRequestDto;
import com.prachi18.college_management_system.DTO.SignUpResponseDto;
import com.prachi18.college_management_system.Services.AuthService;
import com.prachi18.college_management_system.Services.ProfessorInvitationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
     private  final AuthService authService;
     private final ProfessorInvitationService professorInvitationService;

     @PostMapping("/login")
     public ApiResponse<String> login(@RequestBody LoginRequestDTO loginDto, HttpServletResponse response ){
          String token= authService.login(loginDto);
          Cookie cookie =new Cookie("token",token);
          cookie.setHttpOnly(true);
          cookie.setPath("/");
          response.addCookie(cookie);
          return new ApiResponse<>(authService.login(loginDto));
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
}
