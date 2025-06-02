package com.jmanzanog.ailab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jmanzanog.ailab.service.MongoQueryService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mongo")
public class MongoQueryController {

    private final MongoQueryService mongoQueryService;

    @Autowired
    public MongoQueryController(MongoQueryService mongoQueryService) {
        this.mongoQueryService = mongoQueryService;
    }

    /**
     * Endpoint para consultar documentos de cualquier base y colección MongoDB.
     * @param dbName nombre de la base de datos
     * @param collection nombre de la colección
     * @param limit límite de resultados
     * @return lista de documentos como JSON
     */
    @GetMapping("/find")
    public ResponseEntity<List<Map<String, Object>>> findDocuments(
            @RequestParam(defaultValue = "sample_mflix") String db,
            @RequestParam(defaultValue = "movies") String collection,
            @RequestParam(defaultValue = "20") int limit) {
        List<Map<String, Object>> docs = mongoQueryService.findDocuments(db, collection, limit);
        return ResponseEntity.ok(docs);
    }
} 