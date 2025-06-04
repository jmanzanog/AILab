package com.jmanzanog.ailab.service;


import com.jmanzanog.ailab.model.EmbeddingRequest;
import com.jmanzanog.ailab.model.EmbeddingResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmbeddingService {

    /**
     * Envía la petición al endpoint de embeddings y devuelve un Mono con la respuesta tipada.
     *
     * @param request DTO con los datos {model, prompt}
     * @return Mono<EmbeddingResponse>
     */
    Mono<EmbeddingResponse> createEmbedding(EmbeddingRequest request);

    Flux<EmbeddingResponse> createEmbeddingsForAllPages(String db, String collection, int limit, int skip);
}
