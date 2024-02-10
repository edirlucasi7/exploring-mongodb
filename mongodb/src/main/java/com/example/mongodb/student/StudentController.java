package com.example.mongodb.student;

import com.example.mongodb.exception.NotFoundException;
import com.example.mongodb.student.request.StudentRequest;
import com.example.mongodb.student.response.StudentsResponse;
import com.example.mongodb.subject.messageError.ErrorResultBody;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StudentController {

    private final StudentRepository studentRepository;
    private final DuplicateStudentNameValidator duplicateStudentNameValidator;

    public StudentController(StudentRepository studentRepository, DuplicateStudentNameValidator duplicateStudentNameValidator) {
        this.studentRepository = studentRepository;
        this.duplicateStudentNameValidator = duplicateStudentNameValidator;
    }

    @Transactional
    @PostMapping("/api/student/create")
    public ResponseEntity<?> save(@Valid @RequestBody StudentRequest studentRequest) {

        // section where I simulate the in-memory cache in the context of the application
        // this more costly request would not be carried out if I use Redis for example
        if (duplicateStudentNameValidator.invalidatedCache()) {
            duplicateStudentNameValidator.addStudents(studentRepository.findAll().stream().map(Student::getName).toList());
        }

        String studentName = studentRequest.name();
        if (duplicateStudentNameValidator.existingStudent(studentName)) {
            return ResponseEntity.badRequest().body(new ErrorResultBody(List.of("The student: %s already exists".formatted(studentName))));
        }

        Student student = studentRequest.toModel();
        studentRepository.save(student);
        duplicateStudentNameValidator.addStudent(studentName);

        return ResponseEntity.ok("student created with success");
    }

    @GetMapping("/api/students")
    public ResponseEntity<StudentsResponse> findAll() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(new StudentsResponse(students));
    }

    @Transactional
    @PutMapping("/api/student/update/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody StudentRequest studentRequest) {
        Student student = studentRepository.findById(new ObjectId(id)).orElseThrow(NotFoundException::new);
        studentRepository.save(student.update(studentRequest));
        return ResponseEntity.ok("student updated with success");
    }

    @DeleteMapping("/api/student/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        if (!studentRepository.existsById(objectId)) return ResponseEntity.notFound().build();

        studentRepository.deleteById(objectId);

        return ResponseEntity.ok("student with objectId: %s deleted" + id);
    }
}
