package com.example.sevice.batis.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class BatisTriggerService {
    
    private static Connection conn;
    private static Statement stmt;
    public void getTrigger(String[] args) {
 
        try {
            Scanner sc = new Scanner(System.in);
           log.info("Enter full path of the file: ");
            String filePath = sc.next();
            Path path = Paths.get(filePath);

           log.info("Press 1: Execute SQL Script Using Plain Java: ");
           log.info("Press 2: Execute SQL Script Using Apache iBatis: ");
            List<String> sqlLines = Files.readAllLines(path);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    // Running SQL Script using Plain Java
                    for (String sql : sqlLines) {
                       log.info("Query: " + sql);

                        if (sql.contains("SELECT")) {
                            ResultSet rs = stmt.executeQuery(sql);
                            log.info("ID" + "\t" + "Name" + "\t" + "Surname" + "\t" + "Age");
                           log.info("");
                            // Extract data from result set
                            while (rs.next()) {
                                // Retrieve by column name
                                int id = rs.getRow();
                                int age = rs.getInt("age");
                                String name = rs.getString("name");
                                String surname = rs.getString("surname");

                                // Display values
                                log.info(id + "\t" + name + "\t" + surname + "\t" + age);
                               log.info("");
                            }
                        } else
                            stmt.execute(sql);
                    }
//                    break;
                case 2:
                    ScriptRunner scriptExecutor = new ScriptRunner(conn);
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    scriptExecutor.runScript(reader);
                    reader.close();
//                    break;
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
       log.info("Reached End of Code!");
    }
    
}
