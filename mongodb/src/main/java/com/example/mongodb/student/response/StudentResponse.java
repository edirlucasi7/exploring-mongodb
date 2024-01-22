package com.example.mongodb.student.response;

import com.example.mongodb.student.Student;

public class StudentResponse {

    private String name;
    private int age;

    public StudentResponse(Student student) {
        this.name = student.getName();
        this.age = student.getAge();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
