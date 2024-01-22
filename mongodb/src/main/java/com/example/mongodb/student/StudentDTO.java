package com.example.mongodb.student;

public class StudentDTO {

    private String name;
    private int age;

    public StudentDTO(Student student) {
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
