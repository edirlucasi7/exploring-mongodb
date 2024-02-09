package com.example.mongodb.subject;

import org.apache.commons.collections4.SetUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;

@Component
@RequestScope
public class StudentEnrollmentValidator {

    private final List<String> errors = new ArrayList<>();

    public boolean isValid(Set<ObjectId> studentIds, Set<ObjectId> existingStudent) {
        validateStudents(studentIds, existingStudent);

        return errors.isEmpty();
    }

    private void validateStudents(Set<ObjectId> studentIds, Set<ObjectId> existingStudent) {
        boolean allStudentsExist = studentIds.size() == existingStudent.size();
        if (allStudentsExist) return;
        SetUtils.difference(studentIds, existingStudent)
                .forEach(student -> errors.add("student '%s' does not exist".formatted(student.toString())));
    }

    public List<String> getErrors() {
        return errors;
    }
}
