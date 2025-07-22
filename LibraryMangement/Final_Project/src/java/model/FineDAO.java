/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author Admin
 */
public class FineDAO {
    public boolean insertFine(FineDTO fine) throws Exception {
        String sql = "INSERT INTO Fines (BorrowID, Amount, Reason, StatusCode, CreatedAt) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, fine.getBorrowID());
            pst.setDouble(2, fine.getAmount());
            pst.setString(3, fine.getReason());
            pst.setString(4, fine.getStatusCode());
            pst.setDate(5, fine.getCreatedAt());
            return pst.executeUpdate() > 0;
        }
    }

    public List<FineDTO> getFinesByBorrowID(int borrowID) throws Exception {
        String sql = "SELECT f.FineID, f.BorrowID, f.Amount, f.Reason, f.StatusCode, f.CreatedAt, " +
                     "fs.DisplayName AS StatusDisplayName, fs.Description AS StatusDescription, " +
                     "fr.DisplayName AS ReasonDisplayName, fr.Description AS ReasonDescription, " +
                     "u.FullName " +
                     "FROM Fines f " +
                     "JOIN FineStatuses fs ON f.StatusCode = fs.StatusCode " +
                     "JOIN FineReasons fr ON f.Reason = fr.ReasonCode " +
                     "JOIN Borrows b ON f.BorrowID = b.BorrowID " +
                     "JOIN Users u ON b.UserID = u.UserID " +
                     "WHERE f.BorrowID = ?";

        List<FineDTO> list = new ArrayList<>();
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, borrowID);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    FineDTO fine = new FineDTO();
                    fine.setFineID(rs.getInt("FineID"));
                    fine.setBorrowID(rs.getInt("BorrowID"));
                    fine.setAmount(rs.getDouble("Amount"));
                    fine.setReason(rs.getString("Reason"));
                    fine.setStatusCode(rs.getString("StatusCode"));
                    fine.setCreatedAt(rs.getDate("CreatedAt"));

                    fine.setStatusDisplayName(rs.getString("StatusDisplayName"));
                    fine.setStatusDescription(rs.getString("StatusDescription"));
                    fine.setReasonDisplayName(rs.getString("ReasonDisplayName"));
                    fine.setReasonDescription(rs.getString("ReasonDescription"));
                    fine.setFullName(rs.getString("FullName"));

                    list.add(fine);
                }
            }
        }
        return list;
    }


    public boolean updateStatus(int fineID, String newStatus) throws Exception {
        String sql = "UPDATE Fines SET StatusCode = ? WHERE FineID = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, newStatus);
            pst.setInt(2, fineID);
            return pst.executeUpdate() > 0;
        }
    }
    public List<FineDTO> getAllFines() throws Exception {
        String sql = "SELECT f.FineID, f.BorrowID, f.Amount, f.CreatedAt, f.StatusCode, f.Reason, " +
                     "fs.DisplayName AS StatusDisplayName, fs.Description AS StatusDescription, " +
                     "fr.DisplayName AS ReasonDisplayName, fr.Description AS ReasonDescription, " +
                     "u.FullName " +
                     "FROM Fines f " +
                     "JOIN FineStatuses fs ON f.StatusCode = fs.StatusCode " +
                     "JOIN FineReasons fr ON f.Reason = fr.ReasonCode " +
                     "JOIN Borrows b ON f.BorrowID = b.BorrowID " +
                     "JOIN Users u ON b.UserID = u.UserID";

        List<FineDTO> list = new ArrayList<>();
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                FineDTO fine = new FineDTO();
                fine.setFineID(rs.getInt("FineID"));
                fine.setBorrowID(rs.getInt("BorrowID"));
                fine.setAmount(rs.getDouble("Amount"));
                fine.setCreatedAt(rs.getDate("CreatedAt"));
                fine.setStatusCode(rs.getString("StatusCode"));
                fine.setReason(rs.getString("Reason"));
                fine.setStatusDisplayName(rs.getString("StatusDisplayName"));
                fine.setStatusDescription(rs.getString("StatusDescription"));
                fine.setReasonDisplayName(rs.getString("ReasonDisplayName"));
                fine.setReasonDescription(rs.getString("ReasonDescription"));
                fine.setFullName(rs.getString("FullName"));

                list.add(fine);
            }
        }
        return list;
    }

    public List<FineDTO> getFinesByUserID(int userId) throws Exception {
        String sql = "SELECT f.FineID, f.BorrowID, f.Amount, f.CreatedAt, f.StatusCode, f.Reason, " +
                     "fs.DisplayName AS StatusDisplayName, fs.Description AS StatusDescription, " +
                     "fr.DisplayName AS ReasonDisplayName, fr.Description AS ReasonDescription " +
                     "FROM Fines f " +
                     "JOIN FineStatuses fs ON f.StatusCode = fs.StatusCode " +
                     "JOIN FineReasons fr ON f.Reason = fr.ReasonCode " +
                     "JOIN Borrows b ON f.BorrowID = b.BorrowID " +
                     "WHERE b.UserID = ?";

        List<FineDTO> list = new ArrayList<>();
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    FineDTO fine = new FineDTO();
                    fine.setFineID(rs.getInt("FineID"));
                    fine.setBorrowID(rs.getInt("BorrowID"));
                    fine.setAmount(rs.getDouble("Amount"));
                    fine.setCreatedAt(rs.getDate("CreatedAt"));
                    fine.setStatusCode(rs.getString("StatusCode"));
                    fine.setReason(rs.getString("Reason"));
                    fine.setStatusDisplayName(rs.getString("StatusDisplayName"));
                    fine.setStatusDescription(rs.getString("StatusDescription"));
                    fine.setReasonDisplayName(rs.getString("ReasonDisplayName"));
                    fine.setReasonDescription(rs.getString("ReasonDescription"));
                    // Không cần fullName nếu không phải admin
                    list.add(fine);
                }
            }
        }
        return list;
    }
    public List<FineDTO> searchFines(String reason, String status, String name) throws Exception {
        String sql = "SELECT f.FineID, f.BorrowID, f.Amount, f.CreatedAt, f.StatusCode, f.Reason, " +
                     "fs.DisplayName AS StatusDisplayName, fs.Description AS StatusDescription, " +
                     "fr.DisplayName AS ReasonDisplayName, fr.Description AS ReasonDescription, " +
                     "u.FullName " +
                     "FROM Fines f " +
                     "JOIN FineStatuses fs ON f.StatusCode = fs.StatusCode " +
                     "JOIN FineReasons fr ON f.Reason = fr.ReasonCode " +
                     "JOIN Borrows b ON f.BorrowID = b.BorrowID " +
                     "JOIN Users u ON b.UserID = u.UserID " +
                     "WHERE (f.Reason LIKE ? OR ? = '') " +
                     "AND (f.StatusCode LIKE ? OR ? = '') " +
                     "AND (u.FullName LIKE ? OR ? = '')";

        List<FineDTO> list = new ArrayList<>();
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            String reasonWildcard = "%" + (reason == null ? "" : reason.trim()) + "%";
            String statusWildcard = "%" + (status == null ? "" : status.trim()) + "%";
            String nameWildcard = "%" + (name == null ? "" : name.trim()) + "%";

            pst.setString(1, reasonWildcard);
            pst.setString(2, reason == null ? "" : reason.trim());
            pst.setString(3, statusWildcard);
            pst.setString(4, status == null ? "" : status.trim());
            pst.setString(5, nameWildcard);
            pst.setString(6, name == null ? "" : name.trim());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    FineDTO fine = new FineDTO();
                    fine.setFineID(rs.getInt("FineID"));
                    fine.setBorrowID(rs.getInt("BorrowID"));
                    fine.setAmount(rs.getDouble("Amount"));
                    fine.setCreatedAt(rs.getDate("CreatedAt"));
                    fine.setStatusCode(rs.getString("StatusCode"));
                    fine.setReason(rs.getString("Reason"));
                    fine.setStatusDisplayName(rs.getString("StatusDisplayName"));
                    fine.setStatusDescription(rs.getString("StatusDescription"));
                    fine.setReasonDisplayName(rs.getString("ReasonDisplayName"));
                    fine.setReasonDescription(rs.getString("ReasonDescription"));
                    fine.setFullName(rs.getString("FullName"));
                    list.add(fine);
                }
            }
        }
        return list;
    }
    public List<FineDTO> searchFinesByUserID(int userId, String reason, String status) throws Exception {
        String sql = "SELECT f.FineID, f.BorrowID, f.Amount, f.CreatedAt, f.StatusCode, f.Reason, " +
                     "fs.DisplayName AS StatusDisplayName, fs.Description AS StatusDescription, " +
                     "fr.DisplayName AS ReasonDisplayName, fr.Description AS ReasonDescription " +
                     "FROM Fines f " +
                     "JOIN FineStatuses fs ON f.StatusCode = fs.StatusCode " +
                     "JOIN FineReasons fr ON f.Reason = fr.ReasonCode " +
                     "JOIN Borrows b ON f.BorrowID = b.BorrowID " +
                     "WHERE b.UserID = ? " +
                     "AND (f.Reason LIKE ? OR ? = '') " +
                     "AND (f.StatusCode LIKE ? OR ? = '')";

        List<FineDTO> list = new ArrayList<>();
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            String reasonWildcard = "%" + (reason == null ? "" : reason.trim()) + "%";
            String statusWildcard = "%" + (status == null ? "" : status.trim()) + "%";

            pst.setInt(1, userId);
            pst.setString(2, reasonWildcard);
            pst.setString(3, reason == null ? "" : reason.trim());
            pst.setString(4, statusWildcard);
            pst.setString(5, status == null ? "" : status.trim());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    FineDTO fine = new FineDTO();
                    fine.setFineID(rs.getInt("FineID"));
                    fine.setBorrowID(rs.getInt("BorrowID"));
                    fine.setAmount(rs.getDouble("Amount"));
                    fine.setCreatedAt(rs.getDate("CreatedAt"));
                    fine.setStatusCode(rs.getString("StatusCode"));
                    fine.setReason(rs.getString("Reason"));
                    fine.setStatusDisplayName(rs.getString("StatusDisplayName"));
                    fine.setStatusDescription(rs.getString("StatusDescription"));
                    fine.setReasonDisplayName(rs.getString("ReasonDisplayName"));
                    fine.setReasonDescription(rs.getString("ReasonDescription"));
                    list.add(fine);
                }
            }
        }
        return list;
    }

}
