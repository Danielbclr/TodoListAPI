package com.danbramos.todoList.service.implementation;

import com.danbramos.todoList.entity.Task;
import com.danbramos.todoList.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setCompleted(false);
        testTask.setCreatedDate(new Date());
        testTask.setDueDate(new Date());
        testTask.setPriority("High");
    }

    @Test
    void getAllEntities() {
        List<Task> tasks = Arrays.asList(testTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllEntities();

        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getEntityById_Found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Task result = taskService.getEntityById(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getEntityById_NotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Task result = taskService.getEntityById(99L);

        assertNull(result);
        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    void createEntity() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.createEntity(testTask);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateEntity_Found() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setCompleted(true);
        updatedTask.setPriority("Low");
        updatedTask.setDueDate(new Date());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateEntity(1L, updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertTrue(result.getCompleted());
        assertEquals("Low", result.getPriority());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateEntity_NotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Task result = taskService.updateEntity(99L, testTask);

        assertNull(result);
        verify(taskRepository, times(1)).findById(99L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteEntity() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteEntity(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
} 