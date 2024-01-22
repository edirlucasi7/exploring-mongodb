package com.example.mongodb.student.request;

import com.example.mongodb.student.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class StudentRequest {

    @NotBlank(message = "student name is required")
    private String name;

    @NotNull(message = "student dateOfBirth is required")
    private LocalDate dateOfBirth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Student toModel() {
        return new Student(this.name, this.dateOfBirth);
    }
}
