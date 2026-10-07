package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Advices.ApiResponse;
import com.prachi18.college_management_system.DTO.AdminInvitationDTO;
import com.prachi18.college_management_system.DTO.ProfessorInvitationDTO;
import com.prachi18.college_management_system.DTO.ProfessorRequestDTO;
import com.prachi18.college_management_system.Services.AdminInvitationService;
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
    private final AdminInvitationService adminInvitationService;

    @PostMapping("/invite-professor")
    public ApiResponse<String> inviteProfessor(@RequestBody ProfessorInvitationDTO request){
          professorInvitationService.inviteProfessor(request);

          return new ApiResponse<>("Professor invited");
    }

    @PostMapping("/invite-admin")
    public ApiResponse<String> inviteAdmin(@RequestBody AdminInvitationDTO request) {
        adminInvitationService.inviteAdmin(request);

        return new ApiResponse<>("Admin invitation sent successfully");
    }
}
