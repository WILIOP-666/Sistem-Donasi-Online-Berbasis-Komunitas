package com.donasi.dao;

import com.donasi.model.Donor;
import com.donasi.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {
    private DatabaseManager dbManager;
    
    public DonorDAO() {
        dbManager = DatabaseManager.getInstance();
    }
    
    public boolean addDonor(Donor donor) {
        String sql = "INSERT INTO donors (name, email, phone, address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, donor.getName());
            pstmt.setString(2, donor.getEmail());
            pstmt.setString(3, donor.getPhone());
            pstmt.setString(4, donor.getAddress());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        donor.setId(generatedKeys.getInt(1));
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
    
    public Donor getDonorById(int id) {
        String sql = "SELECT * FROM donors WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractDonorFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Donor> getAllDonors() {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM donors ORDER BY name";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                donors.add(extractDonorFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donors;
    }
    
    public boolean updateDonor(Donor donor) {
        String sql = "UPDATE donors SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, donor.getName());
            pstmt.setString(2, donor.getEmail());
            pstmt.setString(3, donor.getPhone());
            pstmt.setString(4, donor.getAddress());
            pstmt.setInt(5, donor.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDonor(int id) {
        String sql = "DELETE FROM donors WHERE id = ?";
        
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
    
    private Donor extractDonorFromResultSet(ResultSet rs) throws SQLException {
        Donor donor = new Donor();
        donor.setId(rs.getInt("id"));
        donor.setName(rs.getString("name"));
        donor.setEmail(rs.getString("email"));
        donor.setPhone(rs.getString("phone"));
        donor.setAddress(rs.getString("address"));
        
        // Convert SQL timestamp to LocalDateTime
        String dateStr = rs.getString("registration_date");
        LocalDateTime registrationDate = LocalDateTime.parse(dateStr.replace(" ", "T"));
        donor.setRegistrationDate(registrationDate);
        
        return donor;
    }
}