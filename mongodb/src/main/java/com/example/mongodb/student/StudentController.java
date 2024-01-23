package com.example.mongodb.student;

import com.example.mongodb.student.request.StudentRequest;
import com.example.mongodb.student.response.StudentsResponse;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    @PostMapping("/api/student/create")
    public ResponseEntity<?> save(@Valid @RequestBody StudentRequest studentRequest) {

        Student student = studentRequest.toModel();
        studentRepository.save(student);

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
        Optional<Student> optionalStudent = studentRepository.findById(new ObjectId(id));
        if (optionalStudent.isEmpty()) return ResponseEntity.notFound().build();

        Student student = optionalStudent.get();
        studentRepository.save(student.update(studentRequest));

        return ResponseEntity.ok("student updated with success");
    }

    @DeleteMapping("/api/student/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Optional<Student> optionalStudent = studentRepository.findById(new ObjectId(id));
        if (optionalStudent.isEmpty()) return ResponseEntity.notFound().build();

        studentRepository.delete(optionalStudent.get());

        return ResponseEntity.ok().build();
    }
}
