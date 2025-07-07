/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class UserDAO {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static final String GetUser = "SELECT * FROM Users ";

    public boolean checkLogin(String username, String password) {
        String sql = GetUser + "WHERE Username = ? AND PasswordHash = ?";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, username.trim());
            pst.setString(2, password.trim());
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserDTO getUserByName(String userName) {
        UserDTO user = null;
        String sql = GetUser + "WHERE Username = ?";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, userName.trim());
            rs = pst.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("UserID");
                String name = rs.getString("Username");
                String password = rs.getString("PasswordHash");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String role = rs.getString("Role");
                boolean status = rs.getBoolean("Status");
                user = new UserDTO(userID, userName, password, fullName, email, role, status);
                if (user == null) {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        String sql = GetUser;
        try {
            con = utils.DBUtils.getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String name = rs.getString("Username");
                String password = rs.getString("PasswordHash");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String role = rs.getString("Role");
                boolean status = rs.getBoolean("Status");
                users.add(new UserDTO(userID, fullName, password, fullName, email, role, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updatePassword(int UserID, String newPassword) {
        String sql = "UPDATE Users SET PasswordHash = ? WHERE UserID = ?";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, newPassword);
            pst.setInt(2, UserID);
            boolean rowinserted = pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProfile(UserDTO user) {
        String sql = "UPDATE Users SET FullName = ?, Email = ? WHERE Username = ?";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, user.getFullName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getUserName());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
