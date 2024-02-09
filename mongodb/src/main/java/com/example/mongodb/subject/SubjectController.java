package com.example.mongodb.subject;

import com.example.mongodb.student.Student;
import com.example.mongodb.student.StudentRepository;
import com.example.mongodb.subject.messageError.ErrorResultBody;
import com.example.mongodb.subject.request.StudentEnrollmentRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final StudentEnrollmentValidator studentEnrollmentValidator;

    public SubjectController(SubjectRepository subjectRepository, StudentRepository studentRepository, StudentEnrollmentValidator studentEnrollmentValidator) {
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.studentEnrollmentValidator = studentEnrollmentValidator;
    }

    @Transactional
    @PostMapping("/api/subject/create")
    public ResponseEntity<?> save(@Valid @RequestBody SubjectRequest subjectRequest) {
        Set<ObjectId> studentIds = subjectRequest.studentsEnrollment().stream().map(StudentEnrollmentRequest::studentId).collect(Collectors.toSet());
        Map<ObjectId, String> existingStudent = studentRepository.findAllByIdIn(studentIds).stream().collect(Collectors.toMap(Student::getId, Student::getName));

        if (!studentEnrollmentValidator.isValid(studentIds, existingStudent.keySet())) {
            return ResponseEntity.badRequest().body(new ErrorResultBody(studentEnrollmentValidator.getErrors()));
        }

        Subject subject = subjectRequest.toModel(existingStudent);
        subjectRepository.save(subject);

        return ResponseEntity.ok("subject created with success");
    }
}