package com.jmanzanog.ailab.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MongoQueryControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.13");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "sample_mflix");
    }

    @Autowired
    private MongoClient mongoClient;

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() throws Exception {
        // Insertar los datos de ejemplo en la colección 'movies'
        MongoDatabase db = mongoClient.getDatabase("sample_mflix");
        MongoCollection<Document> collection = db.getCollection("movies");
        collection.drop();
        String json = """
                [
                  {
                    "plot": "A newly wedded couple attempt to build a house with a prefabricated kit, unaware that a rival sabotaged the kit's component numbering.",
                    "genres": [
                      "Short",
                      "Comedy"
                    ],
                    "runtime": 25,
                    "cast": [
                      "Buster Keaton",
                      "Sybil Seely"
                    ],
                    "num_mflix_comments": 0,
                    "title": "One Week",
                    "fullplot": "Buster and Sybil exit a chapel as newlyweds. Among the gifts is a portable house you easily put together in one week. It doesn't help that Buster's rival for Sybil switches the numbers on the crates containing the house parts.",
                    "languages": [
                      "English"
                    ],
                    "released": "1920-09-01T00:00:00.000+00:00",
                    "directors": [
                      "Edward F. Cline",
                      "Buster Keaton"
                    ],
                    "rated": "TV-G",
                    "awards": {
                      "wins": 1,
                      "nominations": 0,
                      "text": "1 win."
                    },
                    "lastupdated": "2015-05-07 01:07:01.633000000",
                    "year": 1920,
                    "imdb": {
                      "rating": 8.3,
                      "votes": 3942,
                      "id": 11541
                    },
                    "countries": [
                      "USA"
                    ],
                    "type": "movie",
                    "tomatoes": {
                      "viewer": {
                        "rating": 4.3,
                        "numReviews": 752,
                        "meter": 91
                      },
                      "lastUpdated": "2015-09-13T18:22:19.000+00:00"
                    }
                  },
                  {
                    "plot": "The simple-minded son of a rich financier must find his own way in the world.",
                    "genres": [
                      "Comedy"
                    ],
                    "runtime": 77,
                    "cast": [
                      "Edward Jobson",
                      "Beulah Booker",
                      "Edward Connelly",
                      "Edward Alexander"
                    ],
                    "num_mflix_comments": 0,
                    "poster": "https://m.media-amazon.com/images/M/MV5BZDNiODA3NzQtNTBmZS00NTM3LWJlOGMtMDg2NzFiNDU2M2M3XkEyXkFqcGdeQXVyMjUxODE0MDY@._V1_SY1000_SX677_AL_.jpg",
                    "title": "The Saphead",
                    "fullplot": "Nick Van Alstyne owns the Henrietta silver mine and is very rich. His son Bertie is naive and spoiled. His daughter Rose is married to shady investor Mark. Mark wrecks Bertie's wedding plans by making him take the blame for Mark's illegitimate daughter. Mark also nearly ruins the family business by selling off Henrietta stock at too low a price. Bertie, of all people, must come to the rescue on the trading floor.",
                    "countries": [
                      "USA"
                    ],
                    "released": "1920-09-01T00:00:00.000+00:00",
                    "directors": [
                      "Herbert Blachè",
                      "Winchell Smith"
                    ],
                    "writers": [
                      "Bronson Howard (original play \\"The Henrietta\\")",
                      "Victor Mapes (play)",
                      "June Mathis (scenario)",
                      "Winchell Smith (play)"
                    ],
                    "awards": {
                      "wins": 0,
                      "nominations": 1,
                      "text": "1 nomination."
                    },
                    "lastupdated": "2015-06-20 00:38:08.303000000",
                    "year": 1920,
                    "imdb": {
                      "rating": 6.2,
                      "votes": 1020,
                      "id": 11652
                    },
                    "type": "movie",
                    "tomatoes": {
                      "viewer": {
                        "rating": 3.3,
                        "numReviews": 435,
                        "meter": 49
                      },
                      "dvd": "2000-01-11T00:00:00.000+00:00",
                      "lastUpdated": "2015-06-23T19:23:34.000+00:00"
                    }
                  }
                ]
        """;
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> docs = mapper.readValue(json, new TypeReference<>() {});
        docs.forEach(doc -> collection.insertOne(new Document(doc)));
    }

    @Test
    void testFindMovies() throws Exception {
        String url = "http://localhost:" + port + "/api/mongo/find?db=sample_mflix&collection=movies&limit=2";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> result = mapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsKey("title");
    }
} 