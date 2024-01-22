package com.example.mongodb.student;

import java.util.List;

public class StudentsDTO {

    private List<StudentDTO> students;

    public StudentsDTO(List<Student> students) {
        this.students = students.stream().map(StudentDTO::new).toList();
    }

    public List<StudentDTO> getStudents() {
        return students;
    }
}
