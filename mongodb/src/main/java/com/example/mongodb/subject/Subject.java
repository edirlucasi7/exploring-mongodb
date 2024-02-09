package com.example.mongodb.subject;

import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection = "subject")
public class Subject {

    @NotNull
    private ObjectId id;

    @NotBlank
    private String name;

    @NotNull
    private Long code;

    @NotNull
    @Min(value = 6)
    private Long workload;

    @NotNull
    private LocalDate createdAt = LocalDate.now();

    private Set<StudentEnrollment> studentsEnrollment;

    public Subject(String name, Long code, Long workload, Map<ObjectId, String> existingStudent) {
        this.name = name;
        this.code = code;
        this.workload = workload;
        this.studentsEnrollment = existingStudent.entrySet().stream()
                .map(enrollmentRequest ->
                        new StudentEnrollment(enrollmentRequest.getKey(), enrollmentRequest.getValue()))
                .collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }

    public Long getCode() {
        return code;
    }

    public Long getWorkload() {
        return workload;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Set<StudentEnrollment> getStudentsEnrollment() {
        return studentsEnrollment;
    }
}

