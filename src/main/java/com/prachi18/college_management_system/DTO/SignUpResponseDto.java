package com.prachi18.college_management_system.DTO;

import com.prachi18.college_management_system.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {
    private Long id;
    private String email;
    private Role role;
}
