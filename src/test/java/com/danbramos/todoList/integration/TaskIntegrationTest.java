package com.danbramos.todoList.integration;

import com.danbramos.todoList.entity.Task;
import com.danbramos.todoList.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void testTaskCrudOperations() throws Exception {
        // Create a task
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Integration Test Task\",\"description\":\"Test Description\",\"completed\":false,\"priority\":\"High\",\"dueDate\":\"2023-12-31\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Integration Test Task")));

        // Get all tasks - should have one task
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Integration Test Task")));

        // Get the task ID
        Long taskId = taskRepository.findAll().get(0).getId();

        // Get task by ID
        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Integration Test Task")));

        // Update the task
        mockMvc.perform(put("/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"completed\":true,\"priority\":\"Low\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.completed", is(true)));

        // Delete the task
        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isOk());

        // Verify it's deleted
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateMultipleTasks() throws Exception {
        // Create first task
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Task 1\",\"description\":\"Description 1\",\"completed\":false,\"priority\":\"High\"}"))
                .andExpect(status().isOk());

        // Create second task
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Task 2\",\"description\":\"Description 2\",\"completed\":true,\"priority\":\"Low\"}"))
                .andExpect(status().isOk());

        // Verify we have two tasks
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[1].title", is("Task 2")));
    }
} 