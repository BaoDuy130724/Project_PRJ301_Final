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
public class FineReasonDAO {
    public List<FineReasonDTO> getAllReasons() throws Exception {
        String sql = "SELECT ReasonCode, DisplayName, Description FROM FineReasons ORDER BY DisplayName";
        List<FineReasonDTO> list = new ArrayList<>();

        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                FineReasonDTO reason = new FineReasonDTO(
                    rs.getString("ReasonCode"),
                    rs.getString("DisplayName"),
                    rs.getString("Description")
                );
                list.add(reason);
            }
        }
        return list;
    }

    public FineReasonDTO getReasonByCode(String reasonCode) throws Exception {
        String sql = "SELECT ReasonCode, DisplayName, Description FROM FineReasons WHERE ReasonCode = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, reasonCode);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new FineReasonDTO(
                        rs.getString("ReasonCode"),
                        rs.getString("DisplayName"),
                        rs.getString("Description")
                    );
                }
            }
        }
        return null;
    }
}
