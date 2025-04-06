package com.danbramos.todoList.repository;

import com.danbramos.todoList.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testSaveTask() {
        // Create a new task
        Task task = new Task();
        task.setTitle("Repository Test Task");
        task.setDescription("Testing the repository layer");
        task.setCompleted(false);
        task.setCreatedDate(new Date());
        task.setPriority("Medium");

        // Save it
        Task savedTask = taskRepository.save(task);

        // Assertions
        assertNotNull(savedTask);
        assertNotNull(savedTask.getId());
        assertEquals("Repository Test Task", savedTask.getTitle());
    }

    @Test
    void testFindById() {
        // Create and save a task
        Task task = new Task();
        task.setTitle("Find by ID Test");
        task.setDescription("Testing findById method");
        task.setCompleted(true);
        task.setCreatedDate(new Date());
        task.setPriority("Low");

        Task savedTask = taskRepository.save(task);
        Long taskId = savedTask.getId();

        // Find the task by ID
        Optional<Task> foundTask = taskRepository.findById(taskId);

        // Assertions
        assertTrue(foundTask.isPresent());
        assertEquals("Find by ID Test", foundTask.get().getTitle());
        assertTrue(foundTask.get().getCompleted());
    }

    @Test
    void testFindAll() {
        // Create and save multiple tasks
        Task task1 = new Task();
        task1.setTitle("Task One");
        task1.setDescription("First task");
        task1.setCompleted(false);
        
        Task task2 = new Task();
        task2.setTitle("Task Two");
        task2.setDescription("Second task");
        task2.setCompleted(true);

        taskRepository.save(task1);
        taskRepository.save(task2);

        // Find all tasks
        List<Task> tasks = taskRepository.findAll();

        // Assertions
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @Test
    void testDeleteById() {
        // Create and save a task
        Task task = new Task();
        task.setTitle("Delete Test");
        task.setDescription("This task will be deleted");
        
        Task savedTask = taskRepository.save(task);
        Long taskId = savedTask.getId();

        // Delete the task
        taskRepository.deleteById(taskId);

        // Try to find it
        Optional<Task> foundTask = taskRepository.findById(taskId);

        // Assertions
        assertFalse(foundTask.isPresent());
    }

    @Test
    void testUpdateTask() {
        // Create and save a task
        Task task = new Task();
        task.setTitle("Original Title");
        task.setDescription("Original Description");
        task.setCompleted(false);
        
        Task savedTask = taskRepository.save(task);
        Long taskId = savedTask.getId();

        // Update the task
        Optional<Task> foundTaskOpt = taskRepository.findById(taskId);
        assertTrue(foundTaskOpt.isPresent());
        
        Task foundTask = foundTaskOpt.get();
        foundTask.setTitle("Updated Title");
        foundTask.setDescription("Updated Description");
        foundTask.setCompleted(true);
        
        taskRepository.save(foundTask);

        // Find it again and verify updates
        Optional<Task> updatedTaskOpt = taskRepository.findById(taskId);
        assertTrue(updatedTaskOpt.isPresent());
        
        Task updatedTask = updatedTaskOpt.get();
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertTrue(updatedTask.getCompleted());
    }
} 