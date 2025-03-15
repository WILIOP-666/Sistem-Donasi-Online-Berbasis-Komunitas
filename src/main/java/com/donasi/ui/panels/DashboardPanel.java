package com.donasi.ui.panels;

import com.donasi.dao.DonationDAO;
import com.donasi.dao.DonorDAO;
import com.donasi.dao.RecipientDAO;
import com.donasi.model.Donation;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.util.List;

public class DashboardPanel extends JPanel {
    private DonationDAO donationDAO;
    private DonorDAO donorDAO;
    private RecipientDAO recipientDAO;
    
    private JLabel titleLabel;
    private JPanel statsPanel;
    private JLabel totalDonationsLabel;
    private JLabel pendingDonationsLabel;
    private JLabel deliveredDonationsLabel;
    private JTable recentDonationsTable;
    
    public DashboardPanel() {
        donationDAO = new DonationDAO();
        donorDAO = new DonorDAO();
        recipientDAO = new RecipientDAO();
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Stats Panel
        statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        totalDonationsLabel = createStatsLabel("Total Donasi", "0");
        pendingDonationsLabel = createStatsLabel("Donasi Pending", "0");
        deliveredDonationsLabel = createStatsLabel("Donasi Terkirim", "0");
        
        statsPanel.add(totalDonationsLabel);
        statsPanel.add(pendingDonationsLabel);
        statsPanel.add(deliveredDonationsLabel);
        
        add(statsPanel, BorderLayout.CENTER);
        
        // Recent Donations Table
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "Donatur", "Penerima", "Jenis Barang", "Jumlah", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recentDonationsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(recentDonationsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Donasi Terbaru"));
        
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private JPanel createStatsLabel(String title, String value) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        panel.add(titleLabel);
        panel.add(valueLabel);
        
        return panel;
    }
    
    public void loadData() {
        // Load donation statistics
        List<Donation> allDonations = donationDAO.getAllDonations();
        int totalDonations = allDonations.size();
        int pendingDonations = 0;
        int deliveredDonations = 0;
        
        for (Donation donation : allDonations) {
            if ("Pending".equals(donation.getStatus())) {
                pendingDonations++;
            } else if ("Delivered".equals(donation.getStatus())) {
                deliveredDonations++;
            }
        }
        
        // Update stats labels
        updateStatsLabel(totalDonationsLabel, "Total Donasi", String.valueOf(totalDonations));
        updateStatsLabel(pendingDonationsLabel, "Donasi Pending", String.valueOf(pendingDonations));
        updateStatsLabel(deliveredDonationsLabel, "Donasi Terkirim", String.valueOf(deliveredDonations));
        
        // Update recent donations table
        DefaultTableModel model = (DefaultTableModel) recentDonationsTable.getModel();
        model.setRowCount(0); // Clear table
        
        // Get only the 10 most recent donations
        int count = 0;
        for (Donation donation : allDonations) {
            if (count >= 10) break;
            
            String donorName = donorDAO.getDonorById(donation.getDonorId()) != null ? 
                    donorDAO.getDonorById(donation.getDonorId()).getName() : "Unknown";
            
            String recipientName = recipientDAO.getRecipientById(donation.getRecipientId()) != null ? 
                    recipientDAO.getRecipientById(donation.getRecipientId()).getName() : "Unknown";
            
            model.addRow(new Object[] {
                donation.getId(),
                donorName,
                recipientName,
                donation.getItemType(),
                donation.getQuantity(),
                donation.getStatus()
            });
            
            count++;
        }
    }
    
    private void updateStatsLabel(JLabel label, String title, String value) {
        if (label.getParent() instanceof JPanel) {
            JPanel panel = (JPanel) label.getParent();
            if (panel.getComponentCount() >= 2 && panel.getComponent(1) instanceof JLabel) {
                ((JLabel) panel.getComponent(1)).setText(value);
            }
        }
    }
    
    // Method to refresh dashboard data
    public void refresh() {
        loadData();
    }
}