package com.donasi.ui.panels;

import com.donasi.dao.DonorDAO;
import com.donasi.model.Donor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.List;

public class DonorManagementPanel extends JPanel {
    private DonorDAO donorDAO;
    
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JTable donorsTable;
    private DefaultTableModel tableModel;
    
    private int selectedDonorId = -1;
    
    public DonorManagementPanel() {
        donorDAO = new DonorDAO();
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Kelola Donatur");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Donatur"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nama:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Telepon:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Alamat:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addressField = new JTextField(20);
        formPanel.add(addressField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Tambah");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Hapus");
        clearButton = new JButton("Bersihkan");
        
        addButton.addActionListener(e -> addDonor());
        updateButton.addActionListener(e -> updateDonor());
        deleteButton.addActionListener(e -> deleteDonor());
        clearButton.addActionListener(e -> clearForm());
        
        // Initially disable update and delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // Table panel
        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "Nama", "Email", "Telepon", "Alamat"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        donorsTable = new JTable(tableModel);
        donorsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && donorsTable.getSelectedRow() != -1) {
                int row = donorsTable.getSelectedRow();
                selectedDonorId = (Integer) donorsTable.getValueAt(row, 0);
                nameField.setText((String) donorsTable.getValueAt(row, 1));
                emailField.setText((String) donorsTable.getValueAt(row, 2));
                phoneField.setText((String) donorsTable.getValueAt(row, 3));
                addressField.setText((String) donorsTable.getValueAt(row, 4));
                
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(donorsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Donatur"));
        
        // Add panels to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadData() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load donors from database
        List<Donor> donors = donorDAO.getAllDonors();
        
        // Add to table
        for (Donor donor : donors) {
            tableModel.addRow(new Object[] {
                donor.getId(),
                donor.getName(),
                donor.getEmail(),
                donor.getPhone(),
                donor.getAddress()
            });
        }
    }
    
    private void addDonor() {
        try {
            // Validate form
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create donor object
            Donor donor = new Donor(
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim()
            );
            
            // Save to database
            boolean success = donorDAO.addDonor(donor);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Donatur berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan donatur", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateDonor() {
        try {
            // Validate form
            if (selectedDonorId == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih donatur terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create donor object
            Donor donor = new Donor();
            donor.setId(selectedDonorId);
            donor.setName(nameField.getText().trim());
            donor.setEmail(emailField.getText().trim());
            donor.setPhone(phoneField.getText().trim());
            donor.setAddress(addressField.getText().trim());
            
            // Update in database
            boolean success = donorDAO.updateDonor(donor);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Donatur berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate donatur", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteDonor() {
        try {
            // Validate selection
            if (selectedDonorId == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih donatur terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin menghapus donatur ini?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete from database
                boolean success = donorDAO.deleteDonor(selectedDonorId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Donatur berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus donatur", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        selectedDonorId = -1;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        donorsTable.clearSelection();
    }
    
    // Method to refresh panel data
    public void refresh() {
        loadData();
    }