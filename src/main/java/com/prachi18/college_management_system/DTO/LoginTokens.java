package com.prachi18.college_management_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokens {
    private String accessToken;
    private String refreshToken;
}
