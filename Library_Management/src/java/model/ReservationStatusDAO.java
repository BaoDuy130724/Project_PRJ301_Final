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
public class ReservationStatusDAO {
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    public List<ReservationStatusDTO> getAllStatuses() {
        List<ReservationStatusDTO> list = new ArrayList<>();
        String sql = "SELECT StatusCode, DisplayName, Description FROM ReservationStatuses";
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new ReservationStatusDTO(
                        rs.getString("StatusCode"),
                        rs.getString("DisplayName"),
                        rs.getString("Description")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;
    }
    public ReservationStatusDTO getStatusByCode(String statusCode) {
        String sql = "SELECT StatusCode, DisplayName, Description FROM ReservationStatuses WHERE StatusCode = ?";
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, statusCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new ReservationStatusDTO(
                    rs.getString("StatusCode"),
                    rs.getString("DisplayName"),
                    rs.getString("Description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return null;
    }

    private void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
