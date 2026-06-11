package com.prachi18.college_management_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectRequestDTO {
    @NotBlank(message = "Subject name is required")
    private String subjectName;
    @NotNull(message = "Professor id is required")
    private Long professorId;
}
