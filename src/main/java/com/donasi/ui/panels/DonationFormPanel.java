package com.donasi.ui.panels;

import com.donasi.dao.DonationDAO;
import com.donasi.dao.DonorDAO;
import com.donasi.dao.RecipientDAO;
import com.donasi.model.Donation;
import com.donasi.model.Donor;
import com.donasi.model.Recipient;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DonationFormPanel extends JPanel {
    private DonationDAO donationDAO;
    private DonorDAO donorDAO;
    private RecipientDAO recipientDAO;
    
    private JComboBox<Donor> donorComboBox;
    private JComboBox<Recipient> recipientComboBox;
    private JTextField itemTypeField;
    private JTextArea descriptionArea;
    private JSpinner quantitySpinner;
    private JTextField locationField;
    private JTextField latitudeField;
    private JTextField longitudeField;
    private JButton submitButton;
    private JButton clearButton;
    private JButton mapButton;
    
    public DonationFormPanel() {
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
        JLabel titleLabel = new JLabel("Formulir Donasi Baru");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Donor selection
        gbc.gridx