package com.omonteirox.todoApp.services;

import com.omonteirox.todoApp.dtos.TaskRecordDTO;
import com.omonteirox.todoApp.exceptions.NotFoundException;
import com.omonteirox.todoApp.models.TaskModel;
import com.omonteirox.todoApp.repositories.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public List<TaskModel> listAllTasks(){
        var tasks = taskRepository.findAll();
        if (tasks.isEmpty()){
            throw new NotFoundException("Task not found");
        }

        return tasks;
    }
    @Transactional
    public Optional<TaskModel> getOneTaskByTitle(String name){
        var task = taskRepository.findByTitle(name);
        if (task.isEmpty()){
            throw new NotFoundException("Task not found");
        }
        task.get().add(linkTo(methodOn(TaskService.class).listAllTasks()).withSelfRel());
        return task;
    }
    @Transactional
    public Optional<TaskModel> getOneTaskById(UUID id){
        var task = taskRepository.findById(id);
        if (task.isEmpty()){
            return null;
        }
        return task;
    }
    @Transactional
    public TaskModel saveTask(TaskRecordDTO task){
        TaskModel taskModel = new TaskModel();
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        BeanUtils.copyProperties(task, taskModel);
        taskModel.setCreatedAt(Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
        return taskRepository.save(taskModel);
    }
    @Transactional
    public TaskModel updateTaskById(UUID id, TaskRecordDTO task){
        taskRepository.findById(id).ifPresentOrElse((taskModel) -> {
                    LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
                    BeanUtils.copyProperties(task, taskModel);
                    if (taskModel.isCompleted())
                        taskModel.setConcluedAt(Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
                    taskRepository.save(taskModel);
        }
        ,() -> {
            throw new NotFoundException("Task not found");
        });
        return getOneTaskById(id).get();
    }
    @Transactional
    public TaskModel updateTaskByTitle(String name, TaskRecordDTO task){
        taskRepository.findByTitle(name).ifPresentOrElse((taskModel) -> {
                    LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
                    BeanUtils.copyProperties(task, taskModel);
                    if (taskModel.isCompleted())
                        taskModel.setConcluedAt(Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
                    taskRepository.save(taskModel);
                }
                ,() -> {
                    throw new NotFoundException("Task not found");
                });
        return getOneTaskByTitle(name).get();
    }
    @Transactional
    public void deleteTaskById(UUID id){
        taskRepository.findById(id).ifPresentOrElse((taskModel -> taskRepository.delete(taskModel)), () -> {
            throw new NotFoundException("Task not found");
        });

    }
    @Transactional
    public void deleteTaskByTitle(String name){
        taskRepository.findByTitle(name).ifPresentOrElse((taskModel -> taskRepository.delete(taskModel)), () -> {
                throw new NotFoundException("Task not found");
        });

    }
}
