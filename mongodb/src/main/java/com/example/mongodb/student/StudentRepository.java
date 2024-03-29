package com.example.mongodb.student;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StudentRepository extends MongoRepository<Student, UUID> {

    List<Student> findByName(String name);

    Set<Student> findAllByIdIn(Set<ObjectId> studentIds);

    void deleteById(ObjectId id);

    boolean existsById(ObjectId id);

    Optional<Student> findById(ObjectId id);
}
