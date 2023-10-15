package com.example.sevice.batis;

import com.example.sevice.batis.domain.Address;
import com.example.sevice.batis.domain.ClothsSize;
import com.example.sevice.batis.domain.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
public class BatisApplication {

    // JDBC driver name and database URL
    @Value("${mybatis.jdbc-driver}")
    private static String JDBC_DRIVER;
    @Value("${mybatis.db-url}")
    private static String DB_URL;
    @Value("${mybatis.user}")
    private static String USER;
    @Value("${mybatis.pass}")
    private static String PASS;
    private static Connection conn;
    private static Statement stmt;

    public static void main(String[] args) throws Exception {
        // Create source and target objects with nested objects, lists, and a map
        Person sourcePerson = new Person("John", 30);
//        sourcePerson.setAddress(new Address("123 Main St", "City"));
        sourcePerson.setClothsSizes(new ArrayList<>(Arrays.asList((new ClothsSize("XL", "32")), new ClothsSize("L", "30"))));
        sourcePerson.setHasPets(true);

        Person targetPerson = new Person("Doe", 25);
        targetPerson.setAddress(new Address("456 Elm St", "Town"));
        targetPerson.setClothsSizes(new ArrayList<>(Arrays.asList(new ClothsSize("M", "28"), new ClothsSize("S", "26"))));
        targetPerson.setHasPets(false);

        // Compare and map
        compareAndMap(sourcePerson, targetPerson);

        // Print the modified source object
        System.out.println("Source: " + sourcePerson.getName() + ", " + sourcePerson.getAge());
        System.out.println("Source Address: " + sourcePerson.getAddress().getStreet() + ", " + sourcePerson.getAddress().getCity());
        System.out.println("Source Cloths Sizes:");
        for (ClothsSize size : sourcePerson.getClothsSizes()) {
            System.out.println("  Size: " + size.getSize() + ", Inseam: " + size.getInseam());
        }
        System.out.println("Source Has Pets: " + sourcePerson.isHasPets());
    }


    public static void compareAndMap(Object source, Object target) throws Exception {
        // Create BSON representations of source and target objects
        ObjectMapper objectMapper = new ObjectMapper();
        String sourceBson = objectMapper.writeValueAsString(source);
        String targetBson = objectMapper.writeValueAsString(target);

        // Convert BSON strings to BsonDocument
        BsonDocument sourceDoc = BsonDocument.parse(sourceBson);
        BsonDocument targetDoc = BsonDocument.parse(targetBson);

        System.out.println("Source Person: " + sourceDoc);
        System.out.println("Target Person: " + targetDoc);

        // Compare and map differing values
        compareAndMapFields(sourceDoc, targetDoc);

        // Convert the modified BsonDocument back to the source object
        source = objectMapper.readValue(sourceDoc.toJson(), source.getClass());
        System.out.println("source Person after compare and map: " + source);
    }
    private static void compareAndMapFields(BsonDocument sourceDoc, BsonDocument targetDoc) {
        for (String key : targetDoc.keySet()) {
            BsonValue sourceValue = sourceDoc.get(key);
            BsonValue targetValue = targetDoc.get(key);

            if (!sourceValue.equals(targetValue)) {
                if (sourceValue.isDocument() && targetValue.isDocument()) {
                    // Recursive comparison for nested documents
                    compareAndMapFields(sourceValue.asDocument(), targetValue.asDocument());
                } else if (sourceValue.isArray() && targetValue.isArray()) {
                    // Handle lists (arrays)
                    BsonArray sourceArray = sourceValue.asArray();
                    BsonArray targetArray = targetValue.asArray();

                    if (!sourceArray.equals(targetArray)) {
                        // If arrays are different, replace source with target
                        sourceDoc.put(key, targetValue);
                    }
                } else {
                    // For other types, replace source with target
                    sourceDoc.put(key, targetValue);
                }
            }
        }
    }

}
