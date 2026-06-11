package com.prachi18.college_management_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorRequestDTO {
    @NotBlank(message = "Professor name is required")
    private String profName;
    @NotNull(message = "Department id is required")
    private Long deptId;
}
