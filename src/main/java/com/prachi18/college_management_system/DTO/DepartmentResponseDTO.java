package com.prachi18.college_management_system.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentResponseDTO {
    private Long id;
    private String departmentName;
    private String departmentCode;
    private String hodName;
    private Integer studentCount;
    private Integer professorCount;
}
