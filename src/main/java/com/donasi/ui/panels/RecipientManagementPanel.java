package com.donasi.ui.panels;

import com.donasi.dao.RecipientDAO;
import com.donasi.model.Recipient;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
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

public class RecipientManagementPanel extends JPanel {
    private RecipientDAO recipientDAO;
    
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextArea descriptionArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JTable recipientsTable;
    private DefaultTableModel tableModel;
    
    private int selectedRecipientId = -1;
    
    public RecipientManagementPanel() {
        recipientDAO = new RecipientDAO();
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Kelola Penerima Donasi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Penerima"));
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
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descScrollPane, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Tambah");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Hapus");
        clearButton = new JButton("Bersihkan");
        
        addButton.addActionListener(e -> addRecipient());
        updateButton.addActionListener(e -> updateRecipient());
        deleteButton.addActionListener(e -> deleteRecipient());
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
                new String[] {"ID", "Nama", "Email", "Telepon", "Alamat", "Deskripsi"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recipientsTable = new JTable(tableModel);
        recipientsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && recipientsTable.getSelectedRow() != -1) {
                int row = recipientsTable.getSelectedRow();
                selectedRecipientId = (Integer) recipientsTable.getValueAt(row, 0);
                nameField.setText((String) recipientsTable.getValueAt(row, 1));
                emailField.setText((String) recipientsTable.getValueAt(row, 2));
                phoneField.setText((String) recipientsTable.getValueAt(row, 3));
                addressField.setText((String) recipientsTable.getValueAt(row, 4));
                descriptionArea.setText((String) recipientsTable.getValueAt(row, 5));
                
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(recipientsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Penerima"));
        
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
        
        // Load recipients from database
        List<Recipient> recipients = recipientDAO.getAllRecipients();
        
        // Add to table
        for (Recipient recipient : recipients) {
            tableModel.addRow(new Object[] {
                recipient.getId(),
                recipient.getName(),
                recipient.getEmail(),
                recipient.getPhone(),
                recipient.getAddress(),
                recipient.getDescription()
            });
        }
    }
    
    private void addRecipient() {
        try {
            // Validate form
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create recipient object
            Recipient recipient = new Recipient(
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim(),
                    descriptionArea.getText().trim()
            );
            
            // Save to database
            boolean success = recipientDAO.addRecipient(recipient);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Penerima berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan penerima", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateRecipient() {
        try {
            // Validate form
            if (selectedRecipientId == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih penerima terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create recipient object
            Recipient recipient = new Recipient();
            recipient.setId(selectedRecipientId);
            recipient.setName(nameField.getText().trim());
            recipient.setEmail(emailField.getText().trim());
            recipient.setPhone(phoneField.getText().trim());
            recipient.setAddress(addressField.getText().trim());
            recipient.setDescription(descriptionArea.getText().trim());
            
            // Update in database
            boolean success = recipientDAO.updateRecipient(recipient);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Penerima berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate penerima", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteRecipient() {
        try {
            // Validate selection
            if (selectedRecipientId == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih penerima terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin menghapus penerima ini?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete from database
                boolean success = recipientDAO.deleteRecipient(selectedRecipientId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Penerima berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus penerima", "Error", JOptionPane.ERROR_MESSAGE);
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
        descriptionArea.setText("");
        selectedRecipientId = -1;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        recipientsTable.clearSelection();
    }
    
    // Method to refresh panel data
    public void refresh() {
        loadData();
    }