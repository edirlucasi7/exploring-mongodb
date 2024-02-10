package com.example.mongodb.subject;

import com.example.mongodb.subject.messageError.ErrorResultBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice("com.example.mongodb.subject")
public class SubjectApiControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResultBody(errors));
    }
}
