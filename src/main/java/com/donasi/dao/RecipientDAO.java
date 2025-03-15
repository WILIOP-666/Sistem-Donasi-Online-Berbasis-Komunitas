package com.donasi.dao;

import com.donasi.model.Recipient;
import com.donasi.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecipientDAO {
    private DatabaseManager dbManager;
    
    public RecipientDAO() {
        dbManager = DatabaseManager.getInstance();
    }
    
    public boolean addRecipient(Recipient recipient) {
        String sql = "INSERT INTO recipients (name, email, phone, address, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, recipient.getName());
            pstmt.setString(2, recipient.getEmail());
            pstmt.setString(3, recipient.getPhone());
            pstmt.setString(4, recipient.getAddress());
            pstmt.setString(5, recipient.getDescription());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        recipient.setId(generatedKeys.getInt(1));
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
    
    public Recipient getRecipientById(int id) {
        String sql = "SELECT * FROM recipients WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractRecipientFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Recipient> getAllRecipients() {
        List<Recipient> recipients = new ArrayList<>();
        String sql = "SELECT * FROM recipients ORDER BY name";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                recipients.add(extractRecipientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipients;
    }
    
    public boolean updateRecipient(Recipient recipient) {
        String sql = "UPDATE recipients SET name = ?, email = ?, phone = ?, address = ?, description = ? WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, recipient.getName());
            pstmt.setString(2, recipient.getEmail());
            pstmt.setString(3, recipient.getPhone());
            pstmt.setString(4, recipient.getAddress());
            pstmt.setString(5, recipient.getDescription());
            pstmt.setInt(6, recipient.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRecipient(int id) {
        String sql = "DELETE FROM recipients WHERE id = ?";
        
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
    
    private Recipient extractRecipientFromResultSet(ResultSet rs) throws SQLException {
        Recipient recipient = new Recipient();
        recipient.setId(rs.getInt("id"));
        recipient.setName(rs.getString("name"));
        recipient.setEmail(rs.getString("email"));
        recipient.setPhone(rs.getString("phone"));
        recipient.setAddress(rs.getString("address"));
        recipient.setDescription(rs.getString("description"));
        
        // Convert SQL timestamp to LocalDateTime
        String dateStr = rs.getString("registration_date");
        LocalDateTime registrationDate = LocalDateTime.parse(dateStr.replace(" ", "T"));
        recipient.setRegistrationDate(registrationDate);
        
        return recipient;
    }
}