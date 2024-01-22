package com.example.mongodb.student.response;

import com.example.mongodb.student.Student;
import com.example.mongodb.student.response.StudentResponse;

import java.util.List;

public class StudentsResponse {

    private List<StudentResponse> students;

    public StudentsResponse(List<Student> students) {
        this.students = students.stream().map(StudentResponse::new).toList();
    }

    public List<StudentResponse> getStudents() {
        return students;
    }
}
