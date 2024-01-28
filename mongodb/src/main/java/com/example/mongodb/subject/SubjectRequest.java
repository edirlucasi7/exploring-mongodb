package com.example.mongodb.subject;

import com.example.mongodb.subject.request.StudentEnrollmentRequest;
import jakarta.validation.constraints.*;

import java.util.Set;

public record SubjectRequest(
        @NotBlank String name,
        @NotNull Long code,
        @NotNull @Min(value = 6) Long workload,
        Set<StudentEnrollmentRequest> studentsEnrollment
) {

    public Subject toModel() {
        return new Subject(this.name, this.code, this.workload, this.studentsEnrollment);
    }
}
