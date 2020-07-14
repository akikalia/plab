package main.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class DataSource {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection != null) return connection;

        String password = "";
        try {
            Scanner scanner = new Scanner(new File("C:\\Program Files (x86)\\MySQL\\Connector J 8.0\\password.txt"));
            password = scanner.next();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oopp", "root", password);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
