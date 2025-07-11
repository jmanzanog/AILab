package com.jmanzanog.ailab.controller;

import com.jmanzanog.ailab.api.EmbeddingApi;
import com.jmanzanog.ailab.model.ApiEmbeddingExecutePostRequest;
import com.jmanzanog.ailab.model.ExecutionOutput;
import com.jmanzanog.ailab.service.EmbeddingService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmbeddingController implements EmbeddingApi {


    private final EmbeddingService embeddingService;

    @Autowired
    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    /**
     * Endpoint para disparar el proceso de construir los Embbering en MongoDB.
     */
    @Override
    @PostMapping("/execute")
    public ResponseEntity<ExecutionOutput> _apiEmbeddingExecutePost(
            @Parameter(name = "ApiEmbeddingExecutePostRequest", required = true)
            @Valid @RequestBody ApiEmbeddingExecutePostRequest apiEmbeddingExecutePostRequest
    ) {
        // Ejecutar el proceso en segundo plano de forma asíncrona
        embeddingService.createEmbeddingsForAllPages(
                        "sample_mflix",
                        "movies",
                        20000,
                        0
                )
                .collectList()
                .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
                .subscribe();

        // Responder inmediatamente
        var response = new ExecutionOutput();
        return ResponseEntity.ok(response);
    }
}
