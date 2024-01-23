package com.example.mongodb.student.request;

import com.example.mongodb.student.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StudentRequest(@NotBlank(message = "student name is required") String name,
                             @NotNull(message = "student dateOfBirth is required") LocalDate dateOfBirth) {

    public Student toModel() {
        return new Student(this.name, this.dateOfBirth);
    }
}
