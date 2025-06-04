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

/**
 * Service for querying MongoDB collections.
 * Provides methods for paginated document retrieval and counting documents.
 */
@Service
public class MongoQueryService {

    private final MongoClient mongoClient;

    /**
     * Constructs a MongoQueryService with the provided MongoClient.
     *
     * @param mongoClient the MongoDB client
     */
    @Autowired
    public MongoQueryService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * Retrieves documents from a MongoDB collection with a limit.
     *
     * @param db         the database name
     * @param collection the collection name
     * @param limit      the maximum number of documents to return
     * @return a list of documents as key-value maps
     *
     * Example usage:
     * <pre>
     *     findDocuments("myDB", "myCollection", 100);
     * </pre>
     */
    public List<Map<String, Object>> findDocuments(String db, String collection, int limit) {
        return findDocuments(db, collection, 0, limit);
    }

    /**
     * Retrieves documents from a MongoDB collection with pagination.
     *
     * @param db         the database name
     * @param collection the collection name
     * @param skip       the number of documents to skip (for pagination)
     * @param limit      the maximum number of documents to return
     * @return a list of documents as key-value maps
     *
     * Example usage:
     * <pre>
     *     // Get the first page (documents 0 to 99)
     *     findDocuments("myDB", "myCollection", 0, 100);
     *
     *     // Get the second page (documents 100 to 199)
     *     findDocuments("myDB", "myCollection", 100, 100);
     * </pre>
     */
    public List<Map<String, Object>> findDocuments(String db, String collection, int skip, int limit) {
        MongoCollection<Document> coll = getCollection(db, collection);
        List<Map<String, Object>> result = new ArrayList<>();
        // Use try-with-resources for efficient cursor management
        try (var cursor = coll.find().skip(skip).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        }
        return result;
    }

    /**
     * Returns the total number of documents in a MongoDB collection.
     *
     * @param db         the database name
     * @param collection the collection name
     * @return the total number of documents in the collection
     *
     * Example usage:
     * <pre>
     *     long total = countDocuments("myDB", "myCollection");
     * </pre>
     */
    public long countDocuments(String db, String collection) {
        MongoCollection<Document> coll = getCollection(db, collection);
        return coll.countDocuments();
    }

    /**
     * Helper method to get a MongoCollection from the database and collection name.
     *
     * @param db         the database name
     * @param collection the collection name
     * @return the MongoCollection instance
     */
    private MongoCollection<Document> getCollection(String db, String collection) {
        MongoDatabase database = mongoClient.getDatabase(db);
        return database.getCollection(collection);
    }
}
