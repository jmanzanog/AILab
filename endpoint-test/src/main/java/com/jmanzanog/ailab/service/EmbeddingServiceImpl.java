package com.jmanzanog.ailab.service;


import com.jmanzanog.ailab.model.EmbeddingRequest;
import com.jmanzanog.ailab.model.EmbeddingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingServiceImpl extends AbstractWebClientService implements EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingServiceImpl.class);

    private final MongoQueryService mongoQueryService;

    @Value("${pagination.page-size}")
    private int pageSize;

    @Value("${embedding.model}")
    private String embeddingModel;

    @Value("${embedding.prompt-path}")
    private String promptPath;

    @Value("${embedding.concurrency-level:2}")
    private int concurrencyLevel;

    public EmbeddingServiceImpl(WebClient embeddingWebClient, MongoQueryService mongoQueryService) {
        super(embeddingWebClient);
        this.mongoQueryService = mongoQueryService;
    }

    @Override
    public Mono<EmbeddingResponse> createEmbedding(EmbeddingRequest request) {
        return postRequest(request, EmbeddingResponse.class)
                .doOnError(throwable ->
                        log.error("Error al invocar createEmbedding con payload {}: {}", request, throwable.getMessage())
                );
    }

    /**
     * Extrae el valor de un documento dado un path con puntos (soporta anidamiento).
     */
    private Object extractValueByPath(Map<String, Object> doc, String path) {
        String[] keys = path.split("\\.");
        Object value = doc;
        for (String key : keys) {
            if (!(value instanceof Map)) return null;
            value = ((Map<?, ?>) value).get(key);
            if (value == null) return null;
        }
        return value;
    }

    /**
     * Pagina documentos de una colección y crea embeddings por cada página.
     *
     * @param db         base de datos
     * @param collection colección
     * @param limit      máximo de documentos a procesar (opcional, si 0 procesa todos)
     * @param skip       documentos a saltar al inicio (opcional, si 0 empieza desde el principio)
     * @return Flux de EmbeddingResponse por cada página procesada
     */
    public Flux<EmbeddingResponse> createEmbeddingsForAllPages(String db, String collection, int limit, int skip) {
        long total = mongoQueryService.countDocuments(db, collection);
        int effectiveLimit = (limit > 0) ? limit : (int) total;
        int start = Math.max(skip, 0);
        int end = Math.min(start + effectiveLimit, (int) total);
        int numPages = (int) Math.ceil((end - start) / (double) pageSize);

        return Flux.range(0, numPages)
                .flatMap(page -> {
                    int pageSkip = start + page * pageSize;
                    int pageLimit = Math.min(pageSize, end - pageSkip);
                    if (pageLimit <= 0) return Flux.empty();
                    List<Map<String, Object>> docs = mongoQueryService.findDocuments(db, collection, pageSkip, pageLimit);
                    return Flux.fromIterable(docs)
                            .flatMap(doc -> {
                                Object promptValue = extractValueByPath(doc, promptPath);
                                if (promptValue == null) return Flux.empty();
                                var req = new EmbeddingRequest().model(embeddingModel).prompt(promptValue.toString());
                                return createEmbedding(req);
                            }, concurrencyLevel);
                }, concurrencyLevel);
    }

}
