package com.example.mongodb.subject;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, UUID> {

    Optional<Subject> findByCode(Long code);

    void deleteById(ObjectId id);

    boolean existsById(ObjectId id);

    Optional<Subject> findById(ObjectId id);
}
