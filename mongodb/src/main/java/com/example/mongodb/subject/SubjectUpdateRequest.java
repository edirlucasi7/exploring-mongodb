package com.example.mongodb.subject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record SubjectUpdateRequest(@NotBlank String name,
                                   @NotNull Long code,
                                   @NotNull String workload) {}
