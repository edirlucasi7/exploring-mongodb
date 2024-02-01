package com.example.mongodb.student;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DuplicateStudentNameValidator {

    private final Set<String> namesInDatabase = new HashSet<>();

    public void addStudent(String name) {
         namesInDatabase.add(name);
    }

    public void addStudents(List<String> name) {
        namesInDatabase.addAll(name);
    }

    public boolean existingStudent(String name) {
        return namesInDatabase.contains(name);
    }

    public boolean invalidatedCache() {
        return namesInDatabase.isEmpty();
    }

    public Set<String> getNamesInDatabase() {
        return namesInDatabase;
    }
}
