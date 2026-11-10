package dev.cobaltgate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EntitlementApiTest {
    @Autowired MockMvc mvc;

    @Test void createsSubscription() throws Exception {
        mvc.perform(post("/api/v1/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"accountId\":\"acme\",\"plan\":\"business\",\"seats\":3}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.active").value(true));
    }

    @Test void validatesRequest() throws Exception {
        mvc.perform(post("/api/v1/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"accountId\":\"\",\"plan\":\"starter\",\"seats\":0}"))
            .andExpect(status().isBadRequest());
    }
}
