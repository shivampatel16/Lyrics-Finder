package org.example;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class Connect {
    public static void main(String args[]) {
        try {
            Scanner sc = new Scanner(System.in);
            ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
            ConnectionString connectionString = new ConnectionString("mongodb://ShivamGautamDSProject4:ShivamGautamDSProject4@ac-d1nwlm4-shard-00-00.tsumdav.mongodb.net:27017, ac-d1nwlm4-shard-00-01.tsumdav.mongodb.net:27017,ac-d1nwlm4-shard-00-02.tsumdav.mongodb.net:27017/?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1");
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("testdata");
            while (true) {
                System.out.println("Enter your choice:");
                System.out.println("1. Insert string into database collection");
                System.out.println("2. Get all strings from database collection");
                System.out.println("3. Exit");
                int choice = sc.nextInt();
                sc.nextLine();
                if (choice == 3) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                switch (choice) {
                    case 1 -> {
                        System.out.println("Enter string to be added: ");
                        String data = sc.nextLine();
                        InsertOneResult result = collection.insertOne(new Document().append("string", data));
                        System.out.println("Inserted " + data + " into database collection successfully!");
                    }
                    case 2 -> {
                        System.out.println("The strings are: ");
                        FindIterable<Document> iterDoc = collection.find();
                        for (Document document : iterDoc) {
                            System.out.println(document.get("string"));
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
