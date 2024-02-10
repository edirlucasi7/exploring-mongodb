package com.example.mongodb.subject;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.mongodb.utils.StringUtils.removeExtraEmptySpacesAndLines;
import static java.lang.String.valueOf;

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

    @Deprecated
    public Subject() {}

    public Subject(String name, Long code, Long workload, Map<ObjectId, String> existingStudents) {
        this.name = removeExtraEmptySpacesAndLines(name);
        this.code = code;
        this.workload = workload;
        this.studentsEnrollment = existingStudents.entrySet().stream()
                .map(enrollmentRequest ->
                        new StudentEnrollment(valueOf(enrollmentRequest.getKey()), enrollmentRequest.getValue()))
                .collect(Collectors.toSet());
    }

    public void addStudents(Set<StudentEnrollment> studentsEnrollment) {
        this.studentsEnrollment.addAll(studentsEnrollment);
    }

    public void removeStudents(Set<StudentEnrollment> studentsEnrollment) {
        this.studentsEnrollment.removeAll(studentsEnrollment);
    }

    public ObjectId getId() {
        return id;
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

