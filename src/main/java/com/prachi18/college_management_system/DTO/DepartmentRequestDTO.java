package com.prachi18.college_management_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequestDTO {
    @NotBlank(message = "Department name is required")
    private String name;
    @NotBlank(message = "Department code is required")
    @Size(max=4,min=4, message = "Department code must be exactly 4 characters")
    private String deptCode;
    @NotBlank(message = "Hod name is required")
    private String hodName;
}