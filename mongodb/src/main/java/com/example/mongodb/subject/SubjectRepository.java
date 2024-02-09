package com.example.mongodb.subject;

import com.example.mongodb.student.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, UUID> {

    Optional<Subject> findByName(String name);

    void deleteById(ObjectId id);

    boolean existsById(ObjectId id);

    Optional<Subject> findById(ObjectId id);
}
