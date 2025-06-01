package com.jmanzanog.ailab.service;

import com.jmanzanog.ailab.model.VersionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public VersionInfo getLangChainVersions() {

        var versions = new VersionInfo();
        versions.setLangchain4jVersion(langchainVersion);
        versions.setLangchain4jSpringBootStarter(langchainVersion);

        return versions;
    }
}