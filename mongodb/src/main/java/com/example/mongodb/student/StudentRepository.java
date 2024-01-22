package com.example.mongodb.student;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends MongoRepository<Student, UUID> {

    List<Student> findByName();

    Student deleteById(ObjectId id);

    boolean existsById(ObjectId id);

    Student findById(ObjectId id);
}
