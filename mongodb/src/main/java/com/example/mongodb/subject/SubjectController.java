package com.example.mongodb.subject;

import com.example.mongodb.student.Student;
import com.example.mongodb.student.StudentRepository;
import com.example.mongodb.subject.messageError.ErrorResultBody;
import com.example.mongodb.subject.request.StudentEnrollmentRequest;
import com.example.mongodb.subject.response.SubjectsResponse;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.mongodb.utils.StringUtils.removeExtraEmptySpacesAndLines;

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
        String subjectName = removeExtraEmptySpacesAndLines(subjectRequest.name());
        Optional<Subject> optionalSubject = subjectRepository.findByName(subjectName);
        if (optionalSubject.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResultBody(List.of("There is already a subject with the name: %s".formatted(subjectName))));
        }

        Set<ObjectId> studentIds = subjectRequest.studentsEnrollment().stream().map(StudentEnrollmentRequest::studentId).collect(Collectors.toSet());
        Map<ObjectId, String> existingStudent = studentRepository.findAllByIdIn(studentIds).stream().collect(Collectors.toMap(Student::getId, Student::getName));
        if (!studentEnrollmentValidator.isValid(studentIds, existingStudent.keySet())) {
            return ResponseEntity.badRequest().body(new ErrorResultBody(studentEnrollmentValidator.getErrors()));
        }

        Subject subject = subjectRequest.toModel(existingStudent);
        subjectRepository.save(subject);

        return ResponseEntity.ok("subject created with success");
    }

    @GetMapping("/api/subjects")
    public ResponseEntity<?> findAll() {
        List<Subject> subjects = subjectRepository.findAll();
        return ResponseEntity.ok(new SubjectsResponse(subjects));
    }
}