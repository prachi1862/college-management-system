package com.prachi18.college_management_system.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subjectName;

    @ManyToOne
    @JoinColumn(name="professor_id")
    private Professor professor;

    @ManyToMany(mappedBy = "subjects")
    private List<Student> students;
}

