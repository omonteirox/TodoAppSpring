package com.omonteirox.todoApp.repositories;

import com.omonteirox.todoApp.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    Optional<TaskModel> findByTitle(String title);
}
