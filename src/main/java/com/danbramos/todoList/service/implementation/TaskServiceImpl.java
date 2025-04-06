package com.danbramos.todoList.service.implementation;

import com.danbramos.todoList.entity.Task;
import com.danbramos.todoList.repository.TaskRepository;
import com.danbramos.todoList.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public Task updateEntity(Long id, Task entity) {
        Optional<Task> existingTaskOpt = repository.findById(id);
        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();
            
            // Set properties from the input entity
            existingTask.setTitle(entity.getTitle());
            existingTask.setDescription(entity.getDescription());
            existingTask.setCompleted(entity.getCompleted());
            existingTask.setDueDate(entity.getDueDate());
            existingTask.setPriority(entity.getPriority());
            existingTask.setTags(entity.getTags());
            
            // The version will be handled automatically by Hibernate
            return repository.save(existingTask);
        }
        return null;
    }

    @Override
    public void deleteEntity(Long id) {
        repository.deleteById(id);
    }
}
