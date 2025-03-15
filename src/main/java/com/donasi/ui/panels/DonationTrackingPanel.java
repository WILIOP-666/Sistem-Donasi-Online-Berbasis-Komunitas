package com.donasi.ui.panels;

import com.donasi.dao.DonationDAO;
import com.donasi.dao.DonorDAO;
import com.donasi.dao.RecipientDAO;
import com.donasi.model.Donation;
import com.donasi.model.Donor;
import com.donasi.model.Recipient;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DonationTrackingPanel extends JPanel {
    private DonationDAO donationDAO;
    private DonorDAO donorDAO;
    private RecipientDAO recipientDAO;
    
    private JTextField searchField;
    private JComboBox<String> statusFilterComboBox;
    private JTable donationsTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailsButton;
    private JButton updateStatusButton;
    
    public DonationTrackingPanel() {
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
        JLabel titleLabel = new JLabel("Tracking Donasi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Search and filter panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Cari:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        searchField = new JTextField(20);
        searchPanel.add(searchField, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        searchPanel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 0.5;
        statusFilterComboBox = new JComboBox<>(new String[] {"Semua", "Pending", "In Transit", "Delivered", "Cancelled"});
        searchPanel.add(statusFilterComboBox, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        JButton searchButton = new JButton("Cari");
        searchButton.addActionListener(e -> filterDonations());
        searchPanel.add(searchButton, gbc);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Table panel
        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "Donatur", "Penerima", "Jenis Barang", "Jumlah", "Status", "Tanggal"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        donationsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(donationsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Donasi"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        viewDetailsButton = new JButton("Lihat Detail");
        updateStatusButton = new JButton("Update Status");
        
        viewDetailsButton.addActionListener(e -> viewDonationDetails());
        updateStatusButton.addActionListener(e -> updateDonationStatus());
        
        // Initially disable buttons
        viewDetailsButton.setEnabled(false);
        updateStatusButton.setEnabled(false);
        
        donationsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = !e.getValueIsAdjusting() && donationsTable.getSelectedRow() != -1;
            viewDetailsButton.setEnabled(hasSelection);
            updateStatusButton.setEnabled(hasSelection);
        });
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(updateStatusButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load donations from database
        List<Donation> donations = donationDAO.getAllDonations();
        
        // Add to table
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        for (Donation donation : donations) {
            String donorName = donorDAO.getDonorById(donation.getDonorId()) != null ? 
                    donorDAO.getDonorById(donation.getDonorId()).getName() : "Unknown";
            
            String recipientName = recipientDAO.getRecipientById(donation.getRecipientId()) != null ? 
                    recipientDAO.getRecipientById(donation.getRecipientId()).getName() : "Unknown";
            
            tableModel.addRow(new Object[] {
                donation.getId(),
                donorName,
                recipientName,
                donation.getItemType(),
                donation.getQuantity(),
                donation.getStatus(),
                donation.getDonationDate().format(formatter)
            });
        }
    }
    
    private void filterDonations() {
        String searchText = searchField.getText().trim().toLowerCase();
        String statusFilter = (String) statusFilterComboBox.getSelectedItem();
        
        // Clear table
        tableModel.setRowCount(0);
        
        // Load donations from database
        List<Donation> donations = donationDAO.getAllDonations();
        
        // Filter and add to table
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        for (Donation donation : donations) {
            // Apply status filter
            if (!"Semua".equals(statusFilter) && !donation.getStatus().equals(statusFilter)) {
                continue;
            }
            
            String donorName = donorDAO.getDonorById(donation.getDonorId()) != null ? 
                    donorDAO.getDonorById(donation.getDonorId()).getName() : "Unknown";
            
            String recipientName = recipientDAO.getRecipientById(donation.getRecipientId()) != null ? 
                    recipientDAO.getRecipientById(donation.getRecipientId()).getName() : "Unknown";
            
            // Apply text search
            if (!searchText.isEmpty()) {
                boolean matches = donorName.toLowerCase().contains(searchText) ||
                                recipientName.toLowerCase().contains(searchText) ||
                                donation.getItemType().toLowerCase().contains(searchText) ||
                                donation.getDescription().toLowerCase().contains(searchText);
                
                if (!matches) {
                    continue;
                }
            }
            
            tableModel.addRow(new Object[] {
                donation.getId(),
                donorName,
                recipientName,
                donation.getItemType(),
                donation.getQuantity(),
                donation.getStatus(),
                donation.getDonationDate().format(formatter)
            });
        }
    }
    
    private void viewDonationDetails() {
        int row = donationsTable.getSelectedRow();
        if (row == -1) return;
        
        int donationId = (Integer) donationsTable.getValueAt(row, 0);
        Donation donation = donationDAO.getDonationById(donationId);
        
        if (donation != null) {
            Donor donor = donorDAO.getDonorById(donation.getDonorId());
            Recipient recipient = recipientDAO.getRecipientById(donation.getRecipientId());
            
            StringBuilder details = new StringBuilder();
            details.append("ID Donasi: ").append(donation.getId()).append("\n");
            details.append("Tanggal: ").append(donation.getDonationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append("\n\n");
            
            details.append("Donatur: ").append(donor != null ? donor.getName() : "Unknown").append("\n");
            details.append("Email: ").append(donor != null ? donor.getEmail() : "-").append("\n");
            details.append("Telepon: ").append(donor != null ? donor.getPhone() : "-").append("\n\n");
            
            details.append("Penerima: ").append(recipient != null ? recipient.getName() : "Unknown").append("\n");
            details.append("Email: ").append(recipient != null ? recipient.getEmail() : "-").append("\n");
            details.append("Telepon: ").append(recipient != null ? recipient.getPhone() : "-").append("\n\n");
            
            details.append("Jenis Barang: ").append(donation.getItemType()).append("\n");
            details.append("Deskripsi: ").append(donation.getDescription()).append("\n");
            details.append("Jumlah: ").append(donation.getQuantity()).append("\n");
            details.append("Status: ").append(donation.getStatus()).append("\n\n");
            
            details.append("Lokasi Pengantaran: ").append(donation.getDeliveryLocation()).append("\n");
            details.append("Koordinat: ").append(String.format("%.6f, %.6f", donation.getLatitude(), donation.getLongitude()));
            
            JOptionPane.showMessageDialog(this, details.toString(), "Detail Donasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateDonationStatus() {
        int row = donationsTable.getSelectedRow();
        if (row == -1) return;
        
        int donationId = (Integer) donationsTable.getValueAt(row, 0);
        Donation donation = donationDAO.getDonationById(donationId);
        
        if (donation != null) {
            String[] statuses = {"Pending", "In Transit", "Delivered", "Cancelled"};
            String currentStatus = donation.getStatus();
            
            String newStatus = (String) JOptionPane.showInputDialog(
                    this,
                    "Pilih status baru:",
                    "Update Status Donasi",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    statuses,
                    currentStatus);
            
            if (newStatus != null && !newStatus.equals(currentStatus)) {
                boolean success = donationDAO.updateDonationStatus(donationId, newStatus);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Status donasi berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate status donasi", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    // Method to refresh panel data
    public void refresh() {
        loadData();
    }