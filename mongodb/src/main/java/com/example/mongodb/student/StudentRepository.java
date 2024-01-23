package com.example.mongodb.student;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.*;

public interface StudentRepository extends MongoRepository<Student, UUID> {

    List<Student> findByName();

    void deleteById(ObjectId id);

    boolean existsById(ObjectId id);

    Optional<Student> findById(ObjectId id);
}
