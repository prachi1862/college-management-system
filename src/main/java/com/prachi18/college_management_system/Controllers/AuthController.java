package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.LoginRequestDTO;
import com.prachi18.college_management_system.DTO.SignUpRequestDto;
import com.prachi18.college_management_system.DTO.SignUpResponseDto;
import com.prachi18.college_management_system.Services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
     private  final AuthService authService;

     @PostMapping("/login")
     public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginDto, HttpServletResponse response ){
          String token= authService.login(loginDto);
          Cookie cookie =new Cookie("token",token);
          cookie.setHttpOnly(true);
          response.addCookie(cookie);
          return ResponseEntity.ok("Login Successful");
     }

     @PostMapping("/signup")
     public SignUpResponseDto signup(@RequestBody SignUpRequestDto request){
         return authService.signUp(request);
     }
}
