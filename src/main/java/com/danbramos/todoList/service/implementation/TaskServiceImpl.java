package com.danbramos.todoList.service.implementation;

import com.danbramos.todoList.entity.Task;
import com.danbramos.todoList.repository.TaskRepository;
import com.danbramos.todoList.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    @Autowired
    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }
    @Override
    public List<Task> getAllEntities() {
        return repository.findAll();
    }

    @Override
    public Task getEntityById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Task createEntity(Task entity) {
        return repository.save(entity);
    }

    @Override
    public Task updateEntity(Long id, Task entity) {
        if (repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void deleteEntity(Long id) {
        repository.deleteById(id);
    }
}
