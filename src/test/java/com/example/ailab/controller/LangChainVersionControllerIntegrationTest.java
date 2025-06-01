package com.example.ailab.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link LangChainVersionController}.
 * These tests use minimal mocking and test the actual integration between components.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LangChainVersionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${langchain4j.version}")
    private String expectedLangchainVersion;

    /**
     * Test that the /api/langchain/versions endpoint returns the correct version information.
     * This test verifies:
     * 1. The HTTP status is 200 OK
     * 2. The response contains the expected version values from application properties
     */
    @Test
    public void testGetVersionsEndpoint() throws Exception {

        mockMvc.perform(get("/api/langchain/versions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.langchain4j-version", equalTo(expectedLangchainVersion)))
                .andExpect(jsonPath("$.langchain4j-spring-boot-starter", equalTo(expectedLangchainVersion)));
    }
}
