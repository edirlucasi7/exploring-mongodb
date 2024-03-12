package com.example.mongodb.subject.response;

import com.example.mongodb.subject.StudentEnrollment;
import com.example.mongodb.subject.Subject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Set;

import static java.lang.String.valueOf;

public class SubjectResponse {

    public String id;
    private String name;
    private Long code;
    private String workload;
    private LocalDate createdAt;
    private Set<StudentEnrollment> studentsEnrollment;

    public SubjectResponse(Subject subject) {
        this.id = valueOf(subject.getId());
        this.name = subject.getName();
        this.code = subject.getCode();
        this.workload = subject.getWorkload();
        this.createdAt = subject.getCreatedAt();
        this.studentsEnrollment = subject.getStudentsEnrollment();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCode() {
        return code;
    }

    public String getWorkload() {
        return workload;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Set<StudentEnrollment> getStudentsEnrollment() {
        return studentsEnrollment;
    }
}
