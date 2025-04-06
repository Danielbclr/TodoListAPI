package com.danbramos.todoList.controller;

import com.danbramos.todoList.entity.Task;
import com.danbramos.todoList.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task testTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

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
    void getAllTasks() throws Exception {
        List<Task> tasks = Arrays.asList(testTask);
        when(taskService.getAllEntities()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).getAllEntities();
    }

    @Test
    void getTaskById() throws Exception {
        when(taskService.getEntityById(1L)).thenReturn(testTask);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).getEntityById(1L);
    }

    @Test
    void createTask() throws Exception {
        when(taskService.createEntity(any(Task.class))).thenReturn(testTask);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Task\",\"description\":\"Test Description\",\"completed\":false,\"priority\":\"High\"}"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createEntity(any(Task.class));
    }

    @Test
    void updateTask() throws Exception {
        when(taskService.updateEntity(eq(1L), any(Task.class))).thenReturn(testTask);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"completed\":true,\"priority\":\"Low\"}"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).updateEntity(eq(1L), any(Task.class));
    }

    @Test
    void deleteTask() throws Exception {
        doNothing().when(taskService).deleteEntity(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteEntity(1L);
    }
} 