package com.donasi;

import com.donasi.ui.MainFrame;
import com.donasi.util.DatabaseManager;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DonasiApp {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize database
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.initDatabase();
        
        // Start the application UI
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}