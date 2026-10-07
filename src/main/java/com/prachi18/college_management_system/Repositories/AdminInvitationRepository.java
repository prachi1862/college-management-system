package com.prachi18.college_management_system.Repositories;

import com.prachi18.college_management_system.Entities.AdminInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminInvitationRepository extends JpaRepository<AdminInvitation, Long> {
    Optional<AdminInvitation> findByToken(String token);
}
