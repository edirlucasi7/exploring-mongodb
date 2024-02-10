package com.example.mongodb.subject.response;

import com.example.mongodb.subject.Subject;

import java.util.List;

public class SubjectsResponse {

    private final List<SubjectResponse> subjects;

    public SubjectsResponse(List<Subject> subjects) {
        this.subjects = subjects.stream().map(SubjectResponse::new).toList();
    }

    public List<SubjectResponse> getSubjects() {
        return subjects;
    }
}
