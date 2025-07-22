/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;
import utils.EmailUtils;

/**
 *
 * @author Admin
 */
public class ReservationDAO {
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    
    public void autoExpireAllReadyToPickupReservations() {
    while (true) {
        autoExpireReadyToPickupReservations();
        if (!hasMoreReadyToExpire()) {
            break;
        }
    }
}


    public void autoExpireReadyToPickupReservations() {
        String updateReservationsSQL =
            "UPDATE TOP (10) Reservations " +
            "SET StatusCode = 'Expired' " +
            "WHERE StatusCode = 'ReadyToPickup' AND DeadlineToPickup < GETDATE()";

        String updateDetailsSQL =
            "UPDATE ReservationDetails SET StatusCode = 'Expired' " +
            "WHERE ReservationID IN (SELECT ReservationID FROM Reservations " +
            "WHERE StatusCode = 'Expired' AND DeadlineToPickup < GETDATE())";

        try (Connection conn = DBUtils.getConnection()) {
            if (conn == null) return;

            conn.setAutoCommit(false);

            try (PreparedStatement pst1 = conn.prepareStatement(updateReservationsSQL);
                 PreparedStatement pst2 = conn.prepareStatement(updateDetailsSQL)) {

                int affected = pst1.executeUpdate();

                if (affected > 0) {
                    pst2.executeUpdate();
                    System.out.println("✅ Đã hết hạn " + affected + " đơn.");
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasMoreReadyToExpire() {
        String query = "SELECT 1 FROM Reservations WHERE StatusCode = 'ReadyToPickup' AND DeadlineToPickup < GETDATE()";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn != null ? conn.prepareStatement(query) : null;
             ResultSet rs = pst != null ? pst.executeQuery() : null) {

            return rs != null && rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Tạo reservation mới (không gán DeadlineToPickup)
    public boolean createReservationWithQuantity(int userId, int bookId, int quantity) {
        String insertReservationSQL =
            "INSERT INTO Reservations (UserID, ReserveDate, StatusCode) VALUES (?, GETDATE(), 'Pending')";
        String insertDetailsSQL =
            "INSERT INTO ReservationDetails (ReservationID, BookID, StatusCode, Quantity) VALUES (?, ?, 'Pending', ?)";

        try (Connection conn = DBUtils.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pst1 = conn.prepareStatement(insertReservationSQL, Statement.RETURN_GENERATED_KEYS)) {
                pst1.setInt(1, userId);
                int affectedRows = pst1.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                int reservationId;
                try (ResultSet generatedKeys = pst1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservationId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement pst2 = conn.prepareStatement(insertDetailsSQL)) {
                    pst2.setInt(1, reservationId);
                    pst2.setInt(2, bookId);
                    pst2.setInt(3, quantity);
                    pst2.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Chuyển Pending → ReadyToPickup nếu đủ sách
    public void updateReservationsToReady() {
        String selectReadyReservations =
            "SELECT r.ReservationID, r.UserID, u.Email, u.FullName " +
            "FROM Reservations r " +
            "JOIN Users u ON r.UserID = u.UserID " +
            "WHERE r.StatusCode = 'Pending' AND r.ReservationID IN ( " +
            "  SELECT rd.ReservationID FROM ReservationDetails rd " +
            "  JOIN Books b ON rd.BookID = b.BookID " +
            "  GROUP BY rd.ReservationID " +
            "  HAVING SUM(CASE WHEN b.Available >= rd.Quantity THEN 0 ELSE 1 END) = 0 " +
            ")";

        String updateReservationSQL =
            "UPDATE Reservations SET StatusCode = 'ReadyToPickup', DeadlineToPickup = DATEADD(DAY, 3, GETDATE()) " +
            "WHERE ReservationID = ?";
        String updateDetailSQL =
            "UPDATE ReservationDetails SET StatusCode = 'ReadyToPickup' WHERE ReservationID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectReadyReservations);
             ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                int reservationID = rs.getInt("ReservationID");
                String email = rs.getString("Email");
                String fullName = rs.getString("FullName");

                try (PreparedStatement updateResStmt = conn.prepareStatement(updateReservationSQL);
                     PreparedStatement updateDetStmt = conn.prepareStatement(updateDetailSQL)) {

                    conn.setAutoCommit(false);

                    updateResStmt.setInt(1, reservationID);
                    updateResStmt.executeUpdate();

                    updateDetStmt.setInt(1, reservationID);
                    updateDetStmt.executeUpdate();

                    conn.commit();
                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    continue; 
                }

                // ✅ Gửi email sau khi cập nhật thành công
                try {
                    String subject = "Your Reserved Book is Ready for Pickup";
                    String content = "Dear " + fullName + ",\n\n"
                        + "The book(s) you reserved is now available. "
                        + "Please visit the library to pick them up within 3 days.\n\n"
                        + "Thank you.";
                    EmailUtils.sendEmail(email, subject, content);
                } catch (Exception e) {
                    e.printStackTrace(); 
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ReservationDTO> getReservationsByUserId(int userId) {
        autoExpireAllReadyToPickupReservations();
        List<ReservationDTO> list = new ArrayList<>();
        String sql =
            "SELECT r.ReservationID, r.UserID, r.ReserveDate, r.StatusCode, " +
            "rs.DisplayName AS StatusDisplayName, r.DeadlineToPickup " +
            "FROM Reservations r JOIN ReservationStatuses rs ON r.StatusCode = rs.StatusCode " +
            "WHERE r.UserID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReservationDTO r = new ReservationDTO();
                r.setReservationID(rs.getInt("ReservationID"));
                r.setUserID(rs.getInt("UserID"));
                r.setReserveDate(rs.getDate("ReserveDate"));
                r.setDeadlineToPickup(rs.getDate("DeadlineToPickup"));
                r.setStatusCode(rs.getString("StatusCode"));
                r.setStatusDisplayName(rs.getString("StatusDisplayName"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ReservationDTO getReservationById(int reservationId) {
        String sql =
            "SELECT r.ReservationID, r.UserID, r.ReserveDate, r.StatusCode, r.DeadlineToPickup, " +
            "       s.DisplayName AS StatusDisplayName " +
            "FROM Reservations r " +
            "JOIN ReservationStatuses s ON r.StatusCode = s.StatusCode " +
            "WHERE r.ReservationID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setReservationID(rs.getInt("ReservationID"));
                    dto.setUserID(rs.getInt("UserID"));
                    dto.setReserveDate(rs.getDate("ReserveDate"));
                    dto.setStatusCode(rs.getString("StatusCode"));
                    dto.setDeadlineToPickup(rs.getDate("DeadlineToPickup"));
                    dto.setStatusDisplayName(rs.getString("StatusDisplayName"));
                    dto.setDetails(getReservationDetailsById(reservationId));
                    return dto;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ReservationDetailDTO> getReservationDetailsById(int reservationId) {
        List<ReservationDetailDTO> list = new ArrayList<>();
        String sql = "SELECT rd.BookID, b.Title AS BookTitle, rd.Quantity, rd.StatusCode, " +
                     "rs.DisplayName, rs.Description " +
                     "FROM ReservationDetails rd " +
                     "JOIN Books b ON rd.BookID = b.BookID " +
                     "JOIN ReservationStatuses rs ON rd.StatusCode = rs.StatusCode " +
                     "WHERE rd.ReservationID = ?";
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, reservationId);
            rs = pst.executeQuery();
            while (rs.next()) {
                ReservationDetailDTO dto = new ReservationDetailDTO();
                dto.setBookID(rs.getInt("BookID"));
                dto.setBookTitle(rs.getString("BookTitle"));
                dto.setQuantity(rs.getInt("Quantity"));
                dto.setStatusCode(rs.getString("StatusCode")); // ✅ statusCode phải có trong SELECT
                dto.setStatusDisplayName(rs.getString("DisplayName"));
                dto.setStatusDescription(rs.getString("Description"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;
    }


    public boolean cancelReservation(int reservationId, int userId) {
        String updateSQL = "UPDATE Reservations SET StatusCode = 'Canceled' " +
                           "WHERE ReservationID = ? AND UserID = ? AND StatusCode = 'Pending'";
        String updateDetailsSQL = "UPDATE ReservationDetails SET StatusCode = 'Canceled' WHERE ReservationID = ?";

        try (Connection conn = DBUtils.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(updateSQL)) {
                ps1.setInt(1, reservationId);
                ps1.setInt(2, userId);
                int rows = ps1.executeUpdate();

                if (rows > 0) {
                    try (PreparedStatement ps2 = conn.prepareStatement(updateDetailsSQL)) {
                        ps2.setInt(1, reservationId);
                        ps2.executeUpdate();
                    }
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean cancelReservationAsAdmin(int reservationID) throws ClassNotFoundException {
        String sql = "UPDATE Reservations SET status = 'Cancelled' WHERE reservationID = ? AND status = 'Pending'";

        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationID);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean fulfillReservation(int reservationId) {
        String updateReservationSQL =
            "UPDATE Reservations SET StatusCode = 'Fulfilled' WHERE ReservationID = ? AND StatusCode = 'ReadyToPickup'";
        String updateDetailSQL =
            "UPDATE ReservationDetails SET StatusCode = 'Fulfilled' WHERE ReservationID = ?";
        String insertBorrowSQL =
            "INSERT INTO Borrows (UserID, BorrowDate, Status, ExpectedReturnDate) " +
            "VALUES (?, GETDATE(), 'Borrowing', DATEADD(DAY, 7, GETDATE()))";
        String insertBorrowDetailSQL =
            "INSERT INTO BorrowDetails (BorrowID, BookID, Quantity) VALUES (?, ?, ?)";
        String updateBookSQL =
            "UPDATE Books SET Available = Available - ? WHERE BookID = ?";
        String checkAvailableSQL =
            "SELECT Available FROM Books WHERE BookID = ?";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            ReservationDTO reservation = getReservationById(reservationId);
            if (reservation == null || reservation.getDetails() == null || reservation.getDetails().isEmpty()) {
                return false;
            }

            int userId = reservation.getUserID();
            List<ReservationDetailDTO> details = reservation.getDetails();

            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);

            pst = conn.prepareStatement(updateReservationSQL);
            pst.setInt(1, reservationId);
            int updated = pst.executeUpdate();
            pst.close();

            if (updated == 0) {
                conn.rollback();
                return false;
            }

            for (ReservationDetailDTO detail : details) {
                int bookId = detail.getBookID();
                int quantity = detail.getQuantity();

                pst = conn.prepareStatement(checkAvailableSQL);
                pst.setInt(1, bookId);
                rs = pst.executeQuery();

                if (rs.next()) {
                    int available = rs.getInt("Available");
                    if (available < quantity) {
                        conn.rollback();
                        return false;
                    }
                } else {
                    conn.rollback();
                    return false;
                }
                rs.close();
                pst.close();
            }

            pst = conn.prepareStatement(insertBorrowSQL, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, userId);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();

            int borrowId = -1;
            if (rs.next()) {
                borrowId = rs.getInt(1);
            } else {
                conn.rollback();
                return false;
            }
            rs.close();
            pst.close();

            for (ReservationDetailDTO detail : details) {
                int bookId = detail.getBookID();
                int quantity = detail.getQuantity();

                pst = conn.prepareStatement(insertBorrowDetailSQL);
                pst.setInt(1, borrowId);
                pst.setInt(2, bookId);
                pst.setInt(3, quantity);
                pst.executeUpdate();
                pst.close();

                pst = conn.prepareStatement(updateBookSQL);
                pst.setInt(1, quantity);
                pst.setInt(2, bookId);
                pst.executeUpdate();
                pst.close();
            }

            pst = conn.prepareStatement(updateDetailSQL);
            pst.setInt(1, reservationId);
            pst.executeUpdate();
            pst.close();

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                // Optional: silently ignore rollback failure
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ex) {
                // Optional: silently ignore close failure
            }
        }

        return false;
    }
    public List<ReservationDTO> getAllReservations() {
        autoExpireAllReadyToPickupReservations();
        List<ReservationDTO> list = new ArrayList<>();
        String sql =
            "SELECT r.ReservationID, r.UserID, u.FullName, r.ReserveDate, r.StatusCode, r.DeadlineToPickup, " +
            "       s.DisplayName AS StatusDisplayName " +
            "FROM Reservations r " +
            "JOIN ReservationStatuses s ON r.StatusCode = s.StatusCode " +
            "JOIN Users u ON r.UserID = u.UserID";


        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReservationDTO dto = new ReservationDTO();
                dto.setReservationID(rs.getInt("ReservationID"));
                dto.setUserID(rs.getInt("UserID"));
                dto.setFullName(rs.getString("FullName"));
                dto.setReserveDate(rs.getDate("ReserveDate"));
                dto.setStatusCode(rs.getString("StatusCode"));
                dto.setDeadlineToPickup(rs.getDate("DeadlineToPickup"));
                dto.setStatusDisplayName(rs.getString("StatusDisplayName"));
                dto.setDetails(getReservationDetailsById(dto.getReservationID()));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<ReservationDTO> searchReservations(String fromDate, String toDate, String fullName, String status) {
        List<ReservationDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT r.ReservationID, r.UserID, u.FullName, r.ReserveDate, r.StatusCode, r.DeadlineToPickup, " +
            "       s.DisplayName AS StatusDisplayName " +
            "FROM Reservations r " +
            "JOIN ReservationStatuses s ON r.StatusCode = s.StatusCode " +
            "JOIN Users u ON r.UserID = u.UserID WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append("AND r.ReserveDate >= ? ");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append("AND r.ReserveDate <= ? ");
            params.add(Date.valueOf(toDate));
        }
        if (fullName != null && !fullName.trim().isEmpty()) {
            sql.append("AND u.FullName LIKE ? ");
            params.add("%" + fullName.trim() + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND r.StatusCode = ? ");
            params.add(status);
        }

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setReservationID(rs.getInt("ReservationID"));
                    dto.setUserID(rs.getInt("UserID"));
                    dto.setFullName(rs.getString("FullName"));
                    dto.setReserveDate(rs.getDate("ReserveDate"));
                    dto.setStatusCode(rs.getString("StatusCode"));
                    dto.setDeadlineToPickup(rs.getDate("DeadlineToPickup"));
                    dto.setStatusDisplayName(rs.getString("StatusDisplayName"));
                    dto.setDetails(getReservationDetailsById(dto.getReservationID()));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
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
