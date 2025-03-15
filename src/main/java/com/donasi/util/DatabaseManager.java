package com.donasi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:donasi.db";
    
    private DatabaseManager() {
        // Private constructor for singleton pattern
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create donors table
            stmt.execute("CREATE TABLE IF NOT EXISTS donors ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "email TEXT,"
                    + "phone TEXT,"
                    + "address TEXT,"
                    + "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");
            
            // Create recipients table
            stmt.execute("CREATE TABLE IF NOT EXISTS recipients ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "email TEXT,"
                    + "phone TEXT,"
                    + "address TEXT,"
                    + "description TEXT,"
                    + "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");
            
            // Create donations table
            stmt.execute("CREATE TABLE IF NOT EXISTS donations ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "donor_id INTEGER,"
                    + "recipient_id INTEGER,"
                    + "item_type TEXT NOT NULL,"
                    + "description TEXT,"
                    + "quantity INTEGER,"
                    + "status TEXT,"
                    + "donation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "delivery_location TEXT,"
                    + "latitude REAL,"
                    + "longitude REAL,"
                    + "FOREIGN KEY (donor_id) REFERENCES donors(id),"
                    + "FOREIGN KEY (recipient_id) REFERENCES recipients(id)"
                    + ")");
            
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }