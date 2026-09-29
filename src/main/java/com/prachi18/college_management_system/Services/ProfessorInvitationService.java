package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.ProfessorInvitationDTO;
import com.prachi18.college_management_system.DTO.ProfessorSignUpDTO;
import com.prachi18.college_management_system.Entities.ProfessorInvitation;
import com.prachi18.college_management_system.Entities.User;
import com.prachi18.college_management_system.Enums.Role;
import com.prachi18.college_management_system.Repositories.ProfessorInvitationRepository;
import com.prachi18.college_management_system.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessorInvitationService {
    private final ProfessorInvitationRepository professorInvitationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void inviteProfessor(ProfessorInvitationDTO request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("user with this email already exists.");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt= LocalDateTime.now().plusHours(24);
        ProfessorInvitation invitation = ProfessorInvitation.builder()
                .email(request.getEmail())
                .token(token)
                .expiresAt(expiresAt)
                .used(false)
                .build();

        professorInvitationRepository.save(invitation);

        String invitationLink =
                "http://localhost:8080/auth/professor-signup?token=" + token;

        System.out.println("Professor invitation link: " + invitationLink);
    }

    public void professorSignUp(ProfessorSignUpDTO request) {
        ProfessorInvitation invitation = professorInvitationRepository
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

        User professor= User.builder()
                .email(invitation.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PROFESSOR)
                .build();

        invitation.setUsed(true);
        userRepository.save(professor);
    }


}
