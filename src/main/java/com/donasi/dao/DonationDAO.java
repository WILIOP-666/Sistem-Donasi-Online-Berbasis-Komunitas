package com.donasi.dao;

import com.donasi.model.Donation;
import com.donasi.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {
    private DatabaseManager dbManager;
    
    public DonationDAO() {
        dbManager = DatabaseManager.getInstance();
    }
    
    public boolean addDonation(Donation donation) {
        String sql = "INSERT INTO donations (donor_id, recipient_id, item_type, description, quantity, status, delivery_location, latitude, longitude) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, donation.getDonorId());
            pstmt.setInt(2, donation.getRecipientId());
            pstmt.setString(3, donation.getItemType());
            pstmt.setString(4, donation.getDescription());
            pstmt.setInt(5, donation.getQuantity());
            pstmt.setString(6, donation.getStatus());
            pstmt.setString(7, donation.getDeliveryLocation());
            pstmt.setDouble(8, donation.getLatitude());
            pstmt.setDouble(9, donation.getLongitude());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        donation.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Donation getDonationById(int id) {
        String sql = "SELECT * FROM donations WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractDonationFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Donation> getAllDonations() {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donations ORDER BY donation_date DESC";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                donations.add(extractDonationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donations;
    }
    
    public List<Donation> getDonationsByDonorId(int donorId) {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE donor_id = ? ORDER BY donation_date DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, donorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                donations.add(extractDonationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donations;
    }
    
    public List<Donation> getDonationsByRecipientId(int recipientId) {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE recipient_id = ? ORDER BY donation_date DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recipientId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                donations.add(extractDonationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donations;
    }
    
    public boolean updateDonationStatus(int id, String status) {
        String sql = "UPDATE donations SET status = ? WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDonation(Donation donation) {
        String sql = "UPDATE donations SET donor_id = ?, recipient_id = ?, item_type = ?, "
                + "description = ?, quantity = ?, status = ?, delivery_location = ?, "
                + "latitude = ?, longitude = ? WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, donation.getDonorId());
            pstmt.setInt(2, donation.getRecipientId());
            pstmt.setString(3, donation.getItemType());
            pstmt.setString(4, donation.getDescription());
            pstmt.setInt(5, donation.getQuantity());
            pstmt.setString(6, donation.getStatus());
            pstmt.setString(7, donation.getDeliveryLocation());
            pstmt.setDouble(8, donation.getLatitude());
            pstmt.setDouble(9, donation.getLongitude());
            pstmt.setInt(10, donation.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDonation(int id) {
        String sql = "DELETE FROM donations WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Donation extractDonationFromResultSet(ResultSet rs) throws SQLException {
        Donation donation = new Donation();
        donation.setId(rs.getInt("id"));
        donation.setDonorId(rs.getInt("donor_id"));
        donation.setRecipientId(rs.getInt("recipient_id"));
        donation.setItemType(rs.getString("item_type"));
        donation.setDescription(rs.getString("description"));
        donation.setQuantity(rs.getInt("quantity"));
        donation.setStatus(rs.getString("status"));
        donation.setDeliveryLocation(rs.getString("delivery_location"));
        donation.setLatitude(rs.getDouble("latitude"));
        donation.setLongitude(rs.getDouble("longitude"));
        
        // Convert SQL timestamp to LocalDateTime
        String dateStr = rs.getString("donation_date");
        LocalDateTime donationDate = LocalDateTime.parse(dateStr.replace(" ", "T"));
        donation.setDonationDate(donationDate);
        
        return donation;
    }
}