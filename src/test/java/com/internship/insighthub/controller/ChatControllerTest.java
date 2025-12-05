package com.internship.insighthub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.insighthub.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private ChatService chatService;


    @Test
    void chatWithoutAuth_shouldReturnOk() throws Exception {
        String requestJson = """
                {
                  "question": "Hello",
                  "sessionId": null
                }
                """;

        mockMvc.perform(post("/api/chat/simple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }


    @Test
    void chatWithHistory_shouldReturnOk() throws Exception {
        String requestJson = """
                {
                  "question": "Hi with history",
                  "sessionId": 1
                }
                """;

        mockMvc.perform(post("/api/chat/with-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }
}
