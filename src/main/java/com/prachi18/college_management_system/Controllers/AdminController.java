package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.ProfessorInvitationDTO;
import com.prachi18.college_management_system.DTO.ProfessorRequestDTO;
import com.prachi18.college_management_system.Services.ProfessorInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path="/admin")
public class AdminController {
    private final ProfessorInvitationService professorInvitationService;

    @PostMapping("/invite-professor")
    public void inviteProfessor(@RequestBody ProfessorInvitationDTO request){
          log.info("Admin invite endpoint reached");
          professorInvitationService.inviteProfessor(request);
    }
}
