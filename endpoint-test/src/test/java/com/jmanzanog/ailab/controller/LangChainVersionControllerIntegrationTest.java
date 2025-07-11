package com.jmanzanog.ailab.controller;

import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link LangChainVersionController}.
 * These tests use minimal mocking and test the actual integration between components.
 */
@SpringBootTest
@AutoConfigureMockMvc
class LangChainVersionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${langchain4j.version}")
    private String expectedLangchainVersion;

    @MockitoBean
    private MongoClient mongo;

    /**
     * Test that the /api/langchain/versions endpoint returns the correct version information.
     * This test verifies:
     * 1. The HTTP status is 200 OK
     * 2. The response contains the expected version values from application properties
     */
    @Test
    void testGetVersionsEndpoint() throws Exception {
        mockMvc.perform(get("/api/langchain/versions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.langchain4jVersion", equalTo(expectedLangchainVersion)))
                .andExpect(jsonPath("$.langchain4jSpringBootStarter", equalTo(expectedLangchainVersion)));
    }
}