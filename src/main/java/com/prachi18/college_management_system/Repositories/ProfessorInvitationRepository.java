package com.prachi18.college_management_system.Repositories;

import com.prachi18.college_management_system.Entities.ProfessorInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorInvitationRepository extends JpaRepository<ProfessorInvitation, Long> {

     Optional<ProfessorInvitation> findByToken(String token);
}
