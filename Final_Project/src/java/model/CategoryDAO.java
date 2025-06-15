/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Admin
 */
public class CategoryDAO {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static final String getCategory = "SELECT * FROM Categories";
    public CategoryDTO getCategoryByID (int id_input){
        CategoryDTO category = null;
        try {
            String sql = getCategory + " WHERE CategoryID = ?";
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, id_input);
            rs = pst.executeQuery();
            if(rs.next()){
                int CategoryID = rs.getInt("CategoryID");
                String CategoryName = rs.getString("Name");
                category = new CategoryDTO(CategoryID, CategoryName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }
}
