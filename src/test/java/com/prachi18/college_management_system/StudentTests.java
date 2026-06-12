package com.prachi18.college_management_system;

import com.prachi18.college_management_system.DTO.StudentRequestDTO;
import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.Entities.*;
import com.prachi18.college_management_system.Repositories.*;
import com.prachi18.college_management_system.Services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class StudentTests {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AdmissionRecordRepository admissionRecordRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    StudentService studentService;

    @Test
    public void createStudent(){
        AdmissionRecord admissionRecord = AdmissionRecord.builder()
                .fees(80000)
                .build();

        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .admissionRecord(admissionRecord)
                .build();

        var savedStudent = studentRepository.save(student);
        System.out.println(savedStudent);
        System.out.println(admissionRecordRepository.count());
        studentRepository.delete(savedStudent);
        System.out.println(admissionRecordRepository.count());

    }

    @Test
    public void saveStudentInDepartment(){
        Department department = Department.builder()
                .name("CSE")
                .deptCode("1111")
                .hodName("Charu Gaur")
                .build();
        var savedDept= departmentRepository.save(department);
        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .department(savedDept)
                .build();
        var savedStudent = studentRepository.save(student);
        System.out.println(savedStudent);

        System.out.println(savedStudent.getDepartment().getName());
    }

    @Test
    public void saveMultipleStudentsInSameDepartment(){

        Department department = Department.builder()
                .name("Music")
                .deptCode("9999")
                .hodName("kiwiii")
                .build();
        var savedDepartment = departmentRepository.save(department);

        Student student1 = Student.builder()
                .firstName("Rina")
                .lastName("Morris")
                .department(department)
                .build();

        Student student2 = Student.builder()
                .firstName("Hannah")
                .lastName("Wells")
                .department(department)
                .build();
        var savedStudent1 = studentRepository.save(student1);
        var savedStudent2 = studentRepository.save(student2);

        department.setStudents(List.of(savedStudent2,savedStudent1));
        System.out.println(savedStudent1.getFirstName());
        System.out.println(savedStudent2.getDepartment().getName());
        System.out.println(savedStudent1.getDepartment().getName());
        System.out.println(department.getStudents().get(0).getFirstName());
        System.out.println(department.getStudents().get(1).getFirstName());
    }

    @Test
    public void saveSubjectWithProfessor(){
        Professor professor = Professor.builder()
                .profName("Reena Yadav")
                .build();
         var savedProf= professorRepository.save(professor);

        Subject subject = Subject.builder()
                .subjectName("PE")
                .professor(professor)
                .build();
        var savedSub= subjectRepository.save(subject);

        System.out.println(savedSub.getProfessor().getProfName());
    }

    @Test
    public void createStudentWithDepartment(){
        Department department = Department.builder()
                .name("ECE")
                .deptCode("6969")
                .hodName("Lily")
                .build();
        var savedDepartment = departmentRepository.save(department);

        StudentRequestDTO dto= StudentRequestDTO.builder()
                .firstName("Rina")
                .lastName("Morris")
                .build();

        StudentResponseDTO savedStudent= studentService.createStudent(dto);
        System.out.println(savedStudent.getFirstName());
        System.out.println(savedStudent.getDepartmentName());

    }

    @Test
    public void getStudentById(){

        Department department = Department.builder()
                .name("ECE")
                .deptCode("6969")
                .hodName("Lily")
                .build();
        var savedDepartment = departmentRepository.save(department);
        Student student= Student.builder()
                .firstName("Rina")
                .lastName("Morris")
                .department(savedDepartment)
                .build();
        Student savedStudent = studentRepository.save(student);
        StudentResponseDTO fetchedStudent= studentService.getStudentById(savedStudent.getId());
        System.out.println(fetchedStudent.getFirstName()+" "+fetchedStudent.getLastName());
    }

    @Test
    public void getAllStudents(){
        Student student1= Student.builder()
                .firstName("Allie")
                .lastName("Hayes")
                .build();
        Student student2= Student.builder()
                .firstName("Lana")
                .lastName("Condor")
                .build();
        Student student3= Student.builder()
                .firstName("Ella")
                .lastName("Bright")
                .build();
        studentRepository.save(student1);
        studentRepository.save(student2);
        var toBeDeletedStudent= studentRepository.save(student3);

        studentService.deleteStudentById(toBeDeletedStudent.getId());
        Pageable pageable = PageRequest.of(0, 10);
        List<StudentResponseDTO> fetchedStudents= studentService.getAllStudents(pageable);
        for(StudentResponseDTO s: fetchedStudents){
            System.out.println(s.getFirstName()+" "+s.getLastName());
        }
    }
}
