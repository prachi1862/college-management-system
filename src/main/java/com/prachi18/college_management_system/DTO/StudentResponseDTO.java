package com.prachi18.college_management_system.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String departmentName;
}
