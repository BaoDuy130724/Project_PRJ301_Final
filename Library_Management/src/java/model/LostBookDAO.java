/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import utils.DBUtils;

/**
 *
 * @author Admin
 */
public class LostBookDAO {
    private static final String INSERT_LOST_BOOK = 
        "INSERT INTO LostBooks (BorrowID, BookID, Quantity, CreatedAt) VALUES (?, ?, ?, ?)";

    public boolean insertLostBook(int borrowId, int bookId, int quantity) {
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(INSERT_LOST_BOOK)) {
            
            pst.setInt(1, borrowId);
            pst.setInt(2, bookId);
            pst.setInt(3, quantity);
            pst.setDate(4, new Date(System.currentTimeMillis()));
            
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }   
}
