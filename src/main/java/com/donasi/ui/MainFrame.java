package com.donasi.ui;

import com.donasi.ui.panels.DashboardPanel;
import com.donasi.ui.panels.DonationFormPanel;
import com.donasi.ui.panels.DonationTrackingPanel;
import com.donasi.ui.panels.DonorManagementPanel;
import com.donasi.ui.panels.RecipientManagementPanel;
import com.donasi.ui.panels.MapPanel;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private DashboardPanel dashboardPanel;
    private DonationFormPanel donationFormPanel;
    private DonationTrackingPanel donationTrackingPanel;
    private DonorManagementPanel donorManagementPanel;
    private RecipientManagementPanel recipientManagementPanel;
    private MapPanel mapPanel;
    
    public MainFrame() {
        initComponents();
        setupLayout();
        setupMenuBar();
        setupWindowListener();
    }
    
    private void initComponents() {
        // Set frame properties
        setTitle("Sistem Donasi Online Berbasis Komunitas");
        setSize(1024, 768);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        // Initialize panels
        dashboardPanel = new DashboardPanel();
        donationFormPanel = new DonationFormPanel();
        donationTrackingPanel = new DonationTrackingPanel();
        donorManagementPanel = new DonorManagementPanel();
        recipientManagementPanel = new RecipientManagementPanel();
        mapPanel = new MapPanel();
        
        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();
    }
    
    private void setupLayout() {
        // Add panels to tabbed pane
        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Donasi Baru", donationFormPanel);
        tabbedPane.addTab("Tracking Donasi", donationTrackingPanel);
        tabbedPane.addTab("Kelola Donatur", donorManagementPanel);
        tabbedPane.addTab("Kelola Penerima", recipientManagementPanel);
        tabbedPane.addTab("Peta Lokasi", mapPanel);
        
        // Add tabbed pane to frame
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Keluar");
        exitItem.addActionListener(e -> exitApplication());
        fileMenu.add(exitItem);
        
        // Donation menu
        JMenu donationMenu = new JMenu("Donasi");
        JMenuItem newDonationItem = new JMenuItem("Donasi Baru");
        newDonationItem.addActionListener(e -> tabbedPane.setSelectedComponent(donationFormPanel));
        JMenuItem trackDonationItem = new JMenuItem("Tracking Donasi");
        trackDonationItem.addActionListener(e -> tabbedPane.setSelectedComponent(donationTrackingPanel));
        donationMenu.add(newDonationItem);
        donationMenu.add(trackDonationItem);
        
        // Management menu
        JMenu managementMenu = new JMenu("Manajemen");
        JMenuItem donorItem = new JMenuItem("Kelola Donatur");
        donorItem.addActionListener(e -> tabbedPane.setSelectedComponent(donorManagementPanel));
        JMenuItem recipientItem = new JMenuItem("Kelola Penerima");
        recipientItem.addActionListener(e -> tabbedPane.setSelectedComponent(recipientManagementPanel));
        managementMenu.add(donorItem);
        managementMenu.add(recipientItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Bantuan");
        JMenuItem aboutItem = new JMenuItem("Tentang Aplikasi");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(donationMenu);
        menuBar.add(managementMenu);
        menuBar.add(helpMenu);
        
        // Set menu bar to frame
        setJMenuBar(menuBar);
    }
    
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar dari aplikasi?",
                "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Sistem Donasi Online Berbasis Komunitas\n" +
                "Versi 1.0\n" +
                "© 2023 Donasi App\n\n" +
                "Aplikasi untuk menghubungkan donatur dengan individu atau kelompok yang membutuhkan bantuan.",
                "Tentang Aplikasi", JOptionPane.INFORMATION_MESSAGE);
    }
}