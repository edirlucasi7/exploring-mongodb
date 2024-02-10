package com.example.mongodb.subject.validator;

import com.example.mongodb.subject.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class DuplicateValuesForDisciplineValidator implements Validator {

    private final SubjectRepository subjectRepository;

    public DuplicateValuesForDisciplineValidator(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SubjectRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SubjectRequest request = (SubjectRequest)target;
        Optional<Subject> optionalSubject = subjectRepository.findByCode(request.code());
        if (optionalSubject.isPresent()) {
            errors.rejectValue("code", null, "code already exist");
        }
    }
}
