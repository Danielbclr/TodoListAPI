package com.danbramos.todoList.service;

import com.danbramos.todoList.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllEntities();
    Task getEntityById(Long id);
    Task createEntity(Task entity);
    Task updateEntity(Long id, Task entity);
    void deleteEntity(Long id);

}
