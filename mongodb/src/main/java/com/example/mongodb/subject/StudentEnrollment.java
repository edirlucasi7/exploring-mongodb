package com.example.mongodb.subject;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class StudentEnrollment {

    @NotNull
    private ObjectId studentId;

    @NotNull
    private String name;

    @NotNull
    private LocalDate enrollment = LocalDate.now();

    public StudentEnrollment(ObjectId studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public ObjectId getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getEnrollment() {
        return enrollment;
    }
}
