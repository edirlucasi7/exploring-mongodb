package com.example.mongodb.subject;

import com.example.mongodb.subject.request.StudentEnrollmentRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.Set;

public record SubjectRequest(
        @NotBlank String name,
        @NotNull Long code,
        @NotNull @Min(value = 6) Long workload,
        @Valid Set<StudentEnrollmentRequest> studentsEnrollment
) {

    public Subject toModel(Map<ObjectId, String> existingStudent) {
        return new Subject(this.name, this.code, this.workload, existingStudent);
    }
}
