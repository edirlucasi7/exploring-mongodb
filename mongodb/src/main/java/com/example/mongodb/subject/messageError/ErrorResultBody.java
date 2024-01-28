package com.example.mongodb.subject.messageError;

import java.util.List;

public class ErrorResultBody {

    private final List<String> errors;

    public ErrorResultBody(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
