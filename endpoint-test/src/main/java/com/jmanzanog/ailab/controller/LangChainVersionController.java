package com.jmanzanog.ailab.controller;

import com.jmanzanog.ailab.api.LangChainVersionApi;
import com.jmanzanog.ailab.model.VersionInfo;
import com.jmanzanog.ailab.service.LangChainVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes endpoints to retrieve LangChain4j version information.
 */
@RestController
public class LangChainVersionController implements LangChainVersionApi {

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
    @Override
    public ResponseEntity<VersionInfo> _getLangChainVersions() {
        var response = versionService.getLangChainVersions();
        return ResponseEntity.ok(response);
    }

}