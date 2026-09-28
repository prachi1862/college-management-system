package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.LoginRequestDTO;
import com.prachi18.college_management_system.DTO.SignUpRequestDto;
import com.prachi18.college_management_system.DTO.SignUpResponseDto;
import com.prachi18.college_management_system.Entities.User;
import com.prachi18.college_management_system.Enums.Role;
import com.prachi18.college_management_system.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public void login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
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
}
