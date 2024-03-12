package com.example.mongodb.subject;

import com.example.mongodb.subject.request.StudentEnrollmentRequest;
import org.bson.types.ObjectId;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Map;
import java.util.Set;

public record SubjectCreateRequest(
        @NotBlank String name,
        @NotNull Long code,
        @NotNull String workload,
        @Valid Set<StudentEnrollmentRequest> studentsEnrollment
) {

    public Subject toModel(Map<ObjectId, String> existingStudent) {
        return new Subject(this.name, this.code, this.workload, existingStudent);
    }
}
