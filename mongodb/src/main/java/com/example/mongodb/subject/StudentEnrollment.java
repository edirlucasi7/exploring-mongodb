package com.example.mongodb.subject;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class StudentEnrollment {

    @NotNull
    private final String studentId;

    @NotNull
    private final String name;

    @NotNull
    private final LocalDate enrollment = LocalDate.now();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentEnrollment that = (StudentEnrollment) o;
        return Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
