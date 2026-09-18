package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.LoginRequestDTO;
import com.prachi18.college_management_system.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
     private  final AuthService authService;

     @PostMapping("/login")
     public void login(@RequestBody LoginRequestDTO request){
              authService.login(request);
     }
}
