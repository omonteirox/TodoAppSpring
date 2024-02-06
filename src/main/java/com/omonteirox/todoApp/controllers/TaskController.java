package com.omonteirox.todoApp.controllers;

import com.omonteirox.todoApp.dtos.TaskRecordDTO;
import com.omonteirox.todoApp.models.TaskModel;
import com.omonteirox.todoApp.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController

public class TaskController {
    @Autowired
    private TaskService taskService;
    @PostMapping("/tasks")
    public ResponseEntity<TaskModel> saveTask(@RequestBody @Valid TaskRecordDTO taskRecordDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.saveTask(taskRecordDTO));

    }
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskModel>> getAllTasks() {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.listAllTasks());
    }
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Optional<TaskModel>> getOneTaskById(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getOneTaskById(id));
    }
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskModel> updateTaskById(@PathVariable("id") UUID id, @RequestBody @Valid TaskRecordDTO taskRecordDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTaskById(id, taskRecordDTO));
    }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<List<TaskModel>> deleteTaskById(@PathVariable("id") UUID id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
