package com.donasi.ui.panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class MapPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private MapDisplayPanel mapDisplayPanel;
    private JLabel coordinatesLabel;
    
    // Map boundaries (for demo purposes)
    private static final double MIN_LAT = -6.3;
    private static final double MAX_LAT = -6.1;
    private static final double MIN_LON = 106.7;
    private static final double MAX_LON = 106.9;
    
    public MapPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Peta Lokasi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Search panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        searchPanel.add(new JLabel("Cari Lokasi:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        searchField = new JTextField(20);
        searchPanel.add(searchField, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        searchButton = new JButton("Cari");
        searchButton.addActionListener(e -> searchLocation());
        searchPanel.add(searchButton, gbc);
        
        add(searchPanel, BorderLayout.SOUTH);
        
        // Map display panel
        mapDisplayPanel = new MapDisplayPanel();
        add(mapDisplayPanel, BorderLayout.CENTER);
        
        // Coordinates label
        coordinatesLabel = new JLabel("Koordinat: Klik pada peta untuk memilih lokasi");
        coordinatesLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        add(coordinatesLabel, BorderLayout.SOUTH);
    }
    
    private void searchLocation() {
        String location = searchField.getText().trim();
        if (location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan nama lokasi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // In a real application, this would call a geocoding API
        // For demo purposes, we'll just show a message
        JOptionPane.showMessageDialog(this, 
                "Fitur pencarian lokasi akan segera tersedia.\n" +
                "Untuk saat ini, silakan klik pada peta untuk memilih lokasi.", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Inner class for map display
    private class MapDisplayPanel extends JPanel {
        private double selectedLat = -6.2;
        private double selectedLon = 106.8;
        
        public MapDisplayPanel() {
            setPreferredSize(new Dimension(600, 400));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setBackground(Color.WHITE);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Convert pixel coordinates to lat/lon
                    int width = getWidth();
                    int height = getHeight();
                    
                    double lon = MIN_LON + (MAX_LON - MIN_LON) * e.getX() / width;
                    double lat = MAX_LAT - (MAX_LAT - MIN_LAT) * e.getY() / height;
                    
                    selectedLat = lat;
                    selectedLon = lon;
                    
                    // Update coordinates label
                    coordinatesLabel.setText(String.format("Koordinat: %.6f, %.6f", lat, lon));
                    
                    // Repaint to show the selected location
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Draw a simple grid
            g2d.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < 10; i++) {
                int x = i * width / 10;
                int y = i * height / 10;
                g2d.drawLine(x, 0, x, height);
                g2d.drawLine(0, y, width, y);
            }
            
            // Draw some sample landmarks (in a real app, these would be actual locations)
            g2d.setColor(Color.BLUE);
            drawLandmark(g2d, -6.175, 106.827, "Jakarta Pusat");
            drawLandmark(g2d, -6.225, 106.800, "Jakarta Selatan");
            drawLandmark(g2d, -6.150, 106.775, "Jakarta Barat");
            
            // Draw selected location
            g2d.setColor(Color.RED);
            int selectedX = (int) ((selectedLon - MIN_LON) / (MAX_LON - MIN_LON) * width);
            int selectedY = (int) ((MAX_LAT - selectedLat) / (MAX_LAT - MIN_LAT) * height);
            
            g2d.fill(new Ellipse2D.Double(selectedX - 5, selectedY - 5, 10, 10));
            g2d.drawString("Lokasi Terpilih", selectedX + 10, selectedY);
        }
        
        private void drawLandmark(Graphics2D g2d, double lat, double lon, String name) {
            int x = (int) ((lon - MIN_LON) / (MAX_LON - MIN_LON) * getWidth());
            int y = (int) ((MAX_LAT - lat) / (MAX_LAT - MIN_LAT) * getHeight());
            
            g2d.fill(new Ellipse2D.Double(x - 3, y - 3, 6, 6));
            g2d.drawString(name, x + 5, y);
        }
        
        public double getSelectedLat() {
            return selectedLat;
        }
        
        public double getSelectedLon() {
            return selectedLon;
        }
    }
}