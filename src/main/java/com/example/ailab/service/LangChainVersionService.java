package com.example.ailab.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to provide information about LangChain4j versions.
 */
@Service
public class LangChainVersionService {

    @Value("${langchain4j.version:unknown}")
    private String langchainVersion;

    /**
     * Returns a map containing information about the supported LangChain4j versions.
     *
     * @return Map with version information
     */
    public Map<String, String> getLangChainVersions() {
        System.out.println("[DEBUG_LOG] LangChainVersionService.getLangChainVersions() called");
        System.out.println("[DEBUG_LOG] langchainVersion = " + langchainVersion);

        Map<String, String> versions = new HashMap<>();
        versions.put("langchain4j-version", langchainVersion);
        versions.put("langchain4j-spring-boot-starter", langchainVersion);

        System.out.println("[DEBUG_LOG] Returning versions: " + versions);
        return versions;
    }
}
