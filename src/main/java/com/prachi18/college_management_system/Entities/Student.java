package com.prachi18.college_management_system.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name="department_id")
    @JsonIgnore
    private Department department;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="admission_id")
    private AdmissionRecord admissionRecord;

    @ManyToMany
    private List<Subject> subjects= new ArrayList<>();
}

