package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.AdminInvitationDTO;
import com.prachi18.college_management_system.DTO.AdminSignUpDTO;
import com.prachi18.college_management_system.DTO.ProfessorSignUpDTO;
import com.prachi18.college_management_system.Entities.AdminInvitation;
import com.prachi18.college_management_system.Entities.ProfessorInvitation;
import com.prachi18.college_management_system.Entities.User;
import com.prachi18.college_management_system.Enums.Role;
import com.prachi18.college_management_system.Repositories.AdminInvitationRepository;
import com.prachi18.college_management_system.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminInvitationService {
    private final AdminInvitationRepository adminInvitationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void inviteAdmin(AdminInvitationDTO request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User with this email already exists");
        }

        String token= UUID.randomUUID().toString();

        LocalDateTime expiresAt= LocalDateTime.now().plusHours(24);

        AdminInvitation invitation = AdminInvitation.builder()
                .email(request.getEmail())
                .token(token)
                .expiresAt(expiresAt)
                .used(false)
                .build();

        adminInvitationRepository.save(invitation);

        String invitationLink =
                "http://localhost:8080/auth/admin-signup?token=" + token;

        System.out.println("Admin invitation link: " + invitationLink);
    }

    public void adminSignUp(AdminSignUpDTO request) {
        AdminInvitation invitation = adminInvitationRepository
                .findByToken(request.getToken())
                .orElseThrow(() ->
                        new RuntimeException("Invalid invitation token")
                );
        if(invitation.isUsed()) {
            throw new RuntimeException("invitation is already used");
        }

        if(invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("invitation is expired");
        }

        User admin= User.builder()
                .email(invitation.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        invitation.setUsed(true);
        userRepository.save(admin);
        adminInvitationRepository.save(invitation);
    }
}
