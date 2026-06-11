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
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String profName;

    @OneToMany(mappedBy = "professor")
    private List<Subject> subjects;

    @ManyToOne
    @JoinColumn(name= "department_id")
    private Department department;
}
