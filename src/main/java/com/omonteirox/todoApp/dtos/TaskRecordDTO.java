package com.omonteirox.todoApp.dtos;

import com.omonteirox.todoApp.enums.PriorityEnum;
import jakarta.validation.constraints.NotEmpty;

public record TaskRecordDTO(@NotEmpty String title, String description, @NotEmpty boolean completed, PriorityEnum priority) {
}
