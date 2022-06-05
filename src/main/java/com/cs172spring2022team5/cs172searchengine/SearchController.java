package com.cs172spring2022team5.cs172searchengine;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.*;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping(value = "api/", method = RequestMethod.POST)
public class SearchController {

    public static final String output_directory = "/Users/royfeng/Desktop/cs172-search-engine/index";
    public static Analyzer analyzer = new StandardAnalyzer(); // converts text to tokens

    static {
        File fileCheck = new File(output_directory);
        if (!fileCheck.isDirectory()) {
            try {
                createIndex();
            } catch (Exception e) {
                System.out.println("I died because of: " + e);
            }
        }
    }

    public static void createIndex() throws Exception {
        File inputFile = new File("/Users/royfeng/Desktop/cs172-search-engine/pretty_tweet_collection.json"); // file that you are reading from
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        ArrayList<JSONObject> jsonObjectArray = new ArrayList<>();

        Directory index = FSDirectory.open(Paths.get(output_directory)); // stores indexes

        IndexWriterConfig config = new IndexWriterConfig(analyzer); // config that writer uses for analyzing
        IndexWriter indexWriter = new IndexWriter(index, config); // writes the documents to the index

        int errors = 0;
        int runCounter = 0;
        int counter = 0;
        for (String line; (line = reader.readLine()) != null; ) {
            try {
                jsonObjectArray.add(new JSONObject(line));
                counter++;
                if (counter > 1000) {
                    runCounter += counter;
                    indexDocuments(indexWriter, jsonObjectArray);
                    counter = 0;
                    jsonObjectArray.clear();
                    System.out.println(runCounter);
                }
            } catch (Exception e) {
                System.out.println("I died because of: " + e);
                errors++;
            }
        }
        if (counter > 0) {
            runCounter+= counter;
            indexDocuments(indexWriter, jsonObjectArray);
            jsonObjectArray.clear();
            System.out.println(runCounter);
        }
        indexWriter.close();
    }

    public static void indexDocuments(IndexWriter indexWriter, ArrayList<JSONObject> jsonObjectArray) throws IOException {
        // int objCount = 0;
        for (JSONObject element : jsonObjectArray) {
            // objCount++;
            JSONObject temp = element.getJSONObject("_id");
            String _id = temp.getString("$oid");
            String tweet_text = element.getString("tweet_text");

            Document doc;
            doc = new Document();

            doc.add(new TextField("_id", _id, org.apache.lucene.document.Field.Store.YES));
            doc.add(new TextField("tweet_text", tweet_text, org.apache.lucene.document.Field.Store.NO));
            indexWriter.addDocument(doc);
        }
        // System.out.println("Total Objects: " + objCount);
    }

    @GetMapping("search")
    public static ResponseEntity<Object> searchFiles(@RequestParam String queryString) {
        System.out.println("Here's the search: " + queryString);
        try {
            Query query = new QueryParser("tweet_text", analyzer).parse(queryString);

            Directory indexDirectory = FSDirectory.open(Paths.get(output_directory));
            IndexReader indexReader = DirectoryReader.open(indexDirectory);

            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 10);

            // System.out.println("Number of hits: " + topDocs.totalHits);

//            List<Document> documents = new ArrayList<>();
//            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                documents.add(searcher.doc(scoreDoc.doc));
//            }

            //JSONObject[] results = new JSONObject[10];
            JSONArray jsArr = new JSONArray();

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB db = mongoClient.getDB("group5tweeter");
            DBCollection col = db.getCollection("tweet_collection");
            //int ind = 0;

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document temp = searcher.doc(scoreDoc.doc);
                String hitId = temp.get("_id");
                ObjectId hitIdObj = new ObjectId(hitId);
                System.out.println(hitId);
                BasicDBObject queryObj = new BasicDBObject("_id", hitIdObj);

                try (DBCursor cursor = col.find(queryObj)) {
                    while (cursor.hasNext()) {
                        DBObject resultDBO = cursor.next();
                        JSONObject resultJSON = new JSONObject(JSON.serialize(resultDBO));
                        jsArr.put(resultJSON.toMap());
                    }
                }
            }

//            for (JSONObject x : results) {
//                System.out.println(x);
//            }

            return new ResponseEntity<>(jsArr.toList(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("rip " + e);
            e.printStackTrace();
        }
        return null;
    }
}
