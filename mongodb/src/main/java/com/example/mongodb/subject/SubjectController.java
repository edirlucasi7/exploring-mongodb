package com.example.mongodb.subject;

import com.example.mongodb.customValidators.NoNullElements;
import com.example.mongodb.exception.NotFoundException;
import com.example.mongodb.student.Student;
import com.example.mongodb.student.StudentRepository;
import com.example.mongodb.subject.messageError.ErrorResultBody;
import com.example.mongodb.subject.request.StudentEnrollmentRequest;
import com.example.mongodb.subject.response.SubjectsResponse;
import com.example.mongodb.subject.validator.DuplicateValuesForDisciplineValidator;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@Validated
@RestController
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final StudentEnrollmentValidator studentEnrollmentValidator;
    private final DuplicateValuesForDisciplineValidator duplicateValuesForDisciplineValidator;

    public SubjectController(SubjectRepository subjectRepository, StudentRepository studentRepository, StudentEnrollmentValidator studentEnrollmentValidator, DuplicateValuesForDisciplineValidator duplicateValuesForDisciplineValidator) {
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.studentEnrollmentValidator = studentEnrollmentValidator;
        this.duplicateValuesForDisciplineValidator = duplicateValuesForDisciplineValidator;
    }

    @InitBinder("subjectRequest")
    public void initBinder(WebDataBinder webDataBinder) { webDataBinder.addValidators(duplicateValuesForDisciplineValidator); }

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

    @Transactional
    @PutMapping("/api/subject/{code}/add/students")
    public ResponseEntity<?> addEnrollments(@PathVariable Long code,
                                         @NotEmpty(message = "The enrollment list is not empty")
                                         @NoNullElements(message = "The enrollment list cannot contain null values")
                                         @RequestBody List<@Valid StudentEnrollmentRequest> studentEnrollmentRequests) {
        Subject subjectByCode = subjectRepository.findByCode(code).orElseThrow(NotFoundException::new);

        Set<ObjectId> studentIds = studentEnrollmentRequests.stream().map(StudentEnrollmentRequest::studentId).collect(Collectors.toSet());
        Map<ObjectId, String> existingStudents = studentRepository.findAllByIdIn(studentIds).stream().collect(Collectors.toMap(Student::getId, Student::getName));
        if (!studentEnrollmentValidator.isValid(studentIds, existingStudents.keySet())) {
            return ResponseEntity.badRequest().body(new ErrorResultBody(studentEnrollmentValidator.getErrors()));
        }

        Set<StudentEnrollment> studentEnrollments = existingStudents.entrySet().stream().map(enrollmentRequest ->
                        new StudentEnrollment(valueOf(enrollmentRequest.getKey()), enrollmentRequest.getValue())).collect(Collectors.toSet());
        subjectByCode.addStudents(studentEnrollments);
        subjectRepository.save(subjectByCode);

        return ResponseEntity.ok("success");
    }

    @Transactional
    @PutMapping("/api/subject/{code}/add/students")
    public ResponseEntity<?> removeEnrollments(@PathVariable Long code,
                                            @NotEmpty(message = "The enrollment list is not empty")
                                            @NoNullElements(message = "The enrollment list cannot contain null values")
                                            @RequestBody List<@Valid StudentEnrollmentRequest> studentEnrollmentRequests) {
        Subject subjectByCode = subjectRepository.findByCode(code).orElseThrow(NotFoundException::new);

        Set<ObjectId> studentIds = studentEnrollmentRequests.stream().map(StudentEnrollmentRequest::studentId).collect(Collectors.toSet());
        Set<Student> students = studentRepository.findAllByIdIn(studentIds);

        Set<StudentEnrollment> studentEnrollments = students.stream().map(student ->
                new StudentEnrollment(valueOf(student.getId()), student.getName())).collect(Collectors.toSet());
        subjectByCode.removeStudents(studentEnrollments);
        subjectRepository.save(subjectByCode);

        return ResponseEntity.ok("success");
    }

    @GetMapping("/api/subjects")
    public ResponseEntity<?> findAll() {
        List<Subject> subjects = subjectRepository.findAll();
        return ResponseEntity.ok(new SubjectsResponse(subjects));
    }

    @DeleteMapping("/api/subject/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        if (!subjectRepository.existsById(objectId)) return ResponseEntity.notFound().build();

        subjectRepository.deleteById(objectId);

        return ResponseEntity.ok("subject with objectId: %s deleted" + id);
    }
}