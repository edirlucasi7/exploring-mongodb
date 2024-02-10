package com.example.mongodb.subject.request;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;

public record StudentEnrollmentRequest(@NotNull(message = "The studentId cannot be null") ObjectId studentId) {
}
