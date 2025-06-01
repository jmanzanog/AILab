package com.jmanzanog.ailab.controller;

import com.jmanzanog.ailab.service.LangChainVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller that exposes endpoints to retrieve LangChain4j version information.
 */
@RestController
@RequestMapping("/api/langchain")
public class LangChainVersionController {

    private final LangChainVersionService versionService;

    @Autowired
    public LangChainVersionController(LangChainVersionService versionService) {
        this.versionService = versionService;
    }

    /**
     * Endpoint to get information about supported LangChain4j versions.
     *
     * @return JSON response with version information
     */
    @GetMapping("/versions")
    public ResponseEntity<Map<String, String>> getLangChainVersions() {
        var response = versionService.getLangChainVersions();
        return ResponseEntity.ok(response);
    }
}