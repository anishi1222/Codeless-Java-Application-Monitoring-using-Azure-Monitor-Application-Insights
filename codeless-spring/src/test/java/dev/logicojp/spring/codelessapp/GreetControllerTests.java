package dev.logicojp.spring.codelessapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GreetControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void getDefaultMessage() throws Exception {
        mvc.perform(get("/greet"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Hello World!"));
    }

    @Test
    void getNamedMessage() throws Exception {
        mvc.perform(get("/greet/Joe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello Joe!"));
    }

    @Test
    @DirtiesContext
    void updateGreeting() throws Exception {
        mvc.perform(put("/greet/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"greeting\":\"Hola\"}"))
                .andExpect(status().isNoContent());

        mvc.perform(get("/greet/Jose"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hola Jose!"));
    }

    @Test
    void rejectMissingGreeting() throws Exception {
        mvc.perform(put("/greet/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No greeting provided"));
    }

    @Test
    void rejectInvalidJson() throws Exception {
        mvc.perform(put("/greet/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid JSON"));
    }

    @Test
    void reportMpServiceFailureWhenMpUrlIsNotConfigured() throws Exception {
        mvc.perform(get("/greet/greet2mp/Joe"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").value("MP service request failed"));
    }
}
