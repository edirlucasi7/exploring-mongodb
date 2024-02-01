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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        Set<ObjectId> existingStudent = studentRepository.findAllByIdIn(studentIds).stream().map(Student::getId).collect(Collectors.toSet());

        if (!studentEnrollmentValidator.isValid(studentIds, existingStudent)) {
            return ResponseEntity.badRequest().body(new ErrorResultBody(studentEnrollmentValidator.getErrors()));
        }

        Subject subject = subjectRequest.toModel();
        subjectRepository.save(subject);

        return ResponseEntity.ok("subject created with success");
    }
}