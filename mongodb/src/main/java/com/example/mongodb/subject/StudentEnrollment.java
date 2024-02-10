package com.example.mongodb.subject;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class StudentEnrollment {

    @NotNull
    private String studentId;

    @NotNull
    private String name;

    @NotNull
    private LocalDate enrollment = LocalDate.now();

    public StudentEnrollment(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getEnrollment() {
        return enrollment;
    }
}
