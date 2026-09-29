package com.prachi18.college_management_system.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorSignUpDTO {
    private String token;
    private String password;
}
