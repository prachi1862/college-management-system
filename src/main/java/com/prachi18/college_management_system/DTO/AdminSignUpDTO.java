package com.prachi18.college_management_system.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminSignUpDTO {
    private String token;
    private String password;
}
