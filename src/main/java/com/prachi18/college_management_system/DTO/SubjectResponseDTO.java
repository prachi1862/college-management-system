package com.prachi18.college_management_system.DTO;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectResponseDTO {
     private long Id;
     private String subjectName;
     private String professorName;
     private Integer studentCount;
}
