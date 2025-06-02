package com.jmanzanog.ailab.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MongoQueryService {

    private final MongoClient mongoClient;

    @Autowired
    public MongoQueryService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public List<Map<String, Object>> findDocuments(String db, String collection, int limit) {
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Document> coll = database.getCollection(collection);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Document doc : coll.find().limit(limit)) {
            result.add(doc);
        }
        return result;
    }
} 