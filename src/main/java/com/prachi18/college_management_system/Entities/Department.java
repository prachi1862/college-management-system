package com.prachi18.college_management_system.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String deptCode;
    private String hodName;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Student> students;

    @OneToMany(mappedBy = "department")
    private List<Professor> professors;
}
