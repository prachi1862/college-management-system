package com.prachi18.college_management_system.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorResponseDTO {
    private Long id;
    private String profName;
    private String deptName;
    private Integer subjectCount;
}
