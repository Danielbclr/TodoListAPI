package com.danbramos.todoList.controller;

import com.danbramos.todoList.entity.Task;
import com.danbramos.todoList.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAllEntities();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return service.getEntityById(id);
    }

    @PostMapping
    public Task createTask(@RequestBody Task entity) {
        return service.createEntity(entity);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task entity) {
        return service.updateEntity(id, entity);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteEntity(id);
    }
}
