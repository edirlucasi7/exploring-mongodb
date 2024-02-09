package com.example.mongodb.subject.request;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

public record StudentEnrollmentRequest(@NotNull ObjectId studentId) {
}
