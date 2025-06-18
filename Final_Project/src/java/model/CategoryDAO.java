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
public class CategoryDAO {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static final String getCategory = "SELECT * FROM Categories";
    public List<CategoryDTO> getAllCategories (){
        List<CategoryDTO> categories = new ArrayList<>();
        try {
            String sql = getCategory;
            con = utils.DBUtils.getConnection();
            Statement stmt = con.createStatement();
            rs=stmt.executeQuery(sql);
            while(rs.next()){
                int CategoryId = rs.getInt("CategoryId");
                String Name = rs.getString("Name");
                categories.add(new CategoryDTO(CategoryId, Name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
    public String getCategoryNameByID (int id_input){
        String categoryName = "";
        try {
            String sql = getCategory + " WHERE CategoryID = ?";
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, id_input);
            rs = pst.executeQuery();
            if(rs.next()){
                categoryName = rs.getString("Name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryName;
    }
    public int getCategoryIdByName(String name_input){
        int categoryId = -1;
        try {
            String sql = getCategory + " WHERE Name = ?";
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, name_input);
            rs = pst.executeQuery();
            if(rs.next()){
                categoryId = rs.getInt("CategoryID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryId;
    }
}
