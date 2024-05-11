package com.sysaid.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.enums.StatusEnum;

/******************************************************************************/

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**************************** /tasks **************************************/

    @Test
    void testTasks() throws Exception {
        /* GET tasks */
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Task> tasks = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<Task>>() {});

        assertEquals(tasks.size(), 10);

        getTasksWithParams(1);

        getTasksWithExceptions();
    }

    List<Task> getTasksWithParams(Integer amount) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                 .param("amount", amount.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Task> tasks = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<Task>>() {});

        assertEquals(tasks.size(), amount);

        return tasks;

    }

    void getTasksWithExceptions() throws Exception {
        /* Invalid Amount */
         mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("amount", "-1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        /* Invalid option */
        mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .param("option", "banana")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    
    /************************* /tasks/{username} ******************************/

    @Test
    void testUserTasks() throws Exception {
      String username = "meital";
      registerUser(username);

      /* GET user tasks */
      mockMvc.perform(MockMvcRequestBuilders
              .get("/tasks/" + username)
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isOk());

      getUserTasksByStatus(StatusEnum.COMPLETE.get(), 0);
      getUserTasksByStatus(StatusEnum.WISHLIST.get(), 0);

      getUserTasksWithExceptions();

      /* PATCH user tasks */
      patchUserTasks();
    }

    void registerUser(String username) throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
              .post("/users")
              .param("username", username)
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    void getUserTasksWithExceptions() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders
              .get("/tasks/meital1")
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isForbidden());

      mockMvc.perform(MockMvcRequestBuilders
              .get("/tasks/meital")
              .param("status", "abc")
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    void getUserTasksByStatus(String status, Integer expectedAmount) 
    throws Exception {
         MvcResult result  = mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks/meital")
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

         List<Task> tasks = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<Task>>() {});

        assertEquals(tasks.size(), expectedAmount);
    }

    void patchUserTasks() throws Exception {
        List<Task> tasks = getTasksWithParams(2);

        patchUserTask(StatusEnum.COMPLETE.get(), tasks.get(0).getKey());
        patchUserTask(StatusEnum.WISHLIST.get(), tasks.get(1).getKey());
    }

    void patchUserTask(String status, String key) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/tasks/meital")
                .param("status", status)
                .param("key", key)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        getUserTasksByStatus(status, 1);
    }

    /*********************** /tasks/task-of-the-day ***************************/

    @Test
    void testTaskOfTheDay() throws Exception {
        /* GET task of the day */
        String responseBody = getTaskOfTheDay();

        /* GET task of the day again and make sure it's the same task */
        mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks/task-of-the-day")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        getTaskOfTheDay().equals(responseBody);
    }

    String getTaskOfTheDay() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks/task-of-the-day")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        return result.andReturn().getResponse().getContentAsString();
    }
}
