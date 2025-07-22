package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class BorrowDAO {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private static final String GetBorrow = "SELECT b.BorrowID, b.UserID, u.FullName, b.BorrowDate, b.ExpectedReturnDate, b.ReturnDate, b.Status "
            + "FROM Borrows b "
            + "JOIN Users u ON b.UserID = u.UserID ";

    private static final String GetBorrowDetail = "SELECT bd.BorrowID, bd.BookID, bk.Title, bd.Quantity " +
            "FROM BorrowDetails bd " +
            "JOIN Books bk ON bd.BookID = bk.BookID " +
            "WHERE bd.BorrowID = ?";

    private static final String InsertBorrow = 
    "INSERT INTO Borrows (UserID, BorrowDate, ExpectedReturnDate, Status) VALUES (?, ?, ?, ?)";


    private static final String InsertBorrowDetail = "INSERT INTO BorrowDetails (BorrowID, BookID, Quantity) VALUES (?, ?, ?)";

    private static final String UpdateReturn = "UPDATE Borrows SET ReturnDate = ?, Status = ? WHERE BorrowID = ?";
    // viet them
    private static final String UpdateStatus =
        "UPDATE Borrows SET Status = ? WHERE BorrowID = ?";
    
    public List<BorrowDTO> getAllBorrows() {
        List<BorrowDTO> list = new ArrayList<>();
        String sql = GetBorrow + "ORDER BY b.BorrowDate DESC";
        try {
            con = DBUtils.getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            Date today = new Date(System.currentTimeMillis());
            while (rs.next()) {
                int borrowId = rs.getInt("BorrowID");
                int userId = rs.getInt("UserID");
                String fullName = rs.getString("FullName");
                Date borrowDate = rs.getDate("BorrowDate");
                Date returnDate = rs.getDate("ReturnDate");
                String status = rs.getString("Status");
                Date expectedReturnDate = rs.getDate("ExpectedReturnDate");
                if (returnDate == null && expectedReturnDate != null
                    && today.after(expectedReturnDate) && !"Overdue".equals(status)) {
                status = "Overdue";
                updateStatus(borrowId, "Overdue");         // ghi xuống DB
            }

                list.add(new BorrowDTO(borrowId, userId, fullName, borrowDate, expectedReturnDate, returnDate, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BorrowDTO> searchBorrowsByUserName(String keyword) {
        List<BorrowDTO> list = new ArrayList<>();
        String sql = GetBorrow + "WHERE u.FullName LIKE ? ORDER BY b.BorrowDate DESC";
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                int borrowId = rs.getInt("BorrowID");
                int userId = rs.getInt("UserID");
                String fullName = rs.getString("FullName");
                Date borrowDate = rs.getDate("BorrowDate");
                Date returnDate = rs.getDate("ReturnDate");
                String status = rs.getString("Status");
                Date expectedReturnDate = rs.getDate("ExpectedReturnDate");
                list.add(new BorrowDTO(borrowId, userId, fullName, borrowDate, expectedReturnDate, returnDate, status));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<BorrowDTO> searchBorrowsByUserAndDate(int userId, String fromDate, String toDate) {
    List<BorrowDTO> list = new ArrayList<>();
    String sql = GetBorrow + "WHERE b.UserID = ?";
    if (fromDate != null && !fromDate.isEmpty()) {
        sql += " AND b.BorrowDate >= ?";
    }
    if (toDate != null && !toDate.isEmpty()) {
        sql += " AND b.BorrowDate <= ?";
    }
    sql += " ORDER BY b.BorrowDate DESC";

    try {
        con = DBUtils.getConnection();
        pst = con.prepareStatement(sql);

        int index = 1;
        pst.setInt(index++, userId);
        if (fromDate != null && !fromDate.isEmpty()) {
            pst.setDate(index++, Date.valueOf(fromDate));
        }
        if (toDate != null && !toDate.isEmpty()) {
            pst.setDate(index++, Date.valueOf(toDate));
        }

        rs = pst.executeQuery();
        while (rs.next()) {
            int borrowId = rs.getInt("BorrowID");
            String fullName = rs.getString("FullName");
            Date borrowDate = rs.getDate("BorrowDate");
            Date returnDate = rs.getDate("ReturnDate");
            String status = rs.getString("Status");
            Date expectedReturnDate = rs.getDate("ExpectedReturnDate");
            list.add(new BorrowDTO(borrowId, userId, fullName, borrowDate, expectedReturnDate, returnDate, status));

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


    public List<BorrowDTO> getBorrowsByUser(int userId_input) {
        List<BorrowDTO> list = new ArrayList<>();
        String sql = GetBorrow + "WHERE b.UserID = ? ORDER BY b.BorrowDate DESC";
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, userId_input);
            rs = pst.executeQuery();
            while (rs.next()) {
                int borrowId = rs.getInt("BorrowID");
                int userId = rs.getInt("UserID");
                String fullName = rs.getString("FullName");
                Date borrowDate = rs.getDate("BorrowDate");
                Date returnDate = rs.getDate("ReturnDate");
                String status = rs.getString("Status");
                Date expectedReturnDate = rs.getDate("ExpectedReturnDate");
                list.add(new BorrowDTO(borrowId, userId, fullName, borrowDate, expectedReturnDate, returnDate, status));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BorrowDetailDTO> getBorrowDetails(int borrowId_input) {
        List<BorrowDetailDTO> list = new ArrayList<>();
        String sql = GetBorrowDetail;
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, borrowId_input);
            rs = pst.executeQuery();
            while (rs.next()) {
                int borrowId = rs.getInt("BorrowID");
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                int quantity = rs.getInt("Quantity");
                list.add(new BorrowDetailDTO(borrowId, bookId, title, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int createBorrow(BorrowDTO borrow) {
        int borrowId = 0;
        String sql = InsertBorrow;
        if (borrow.getExpectedReturnDate() == null) {
            LocalDate expected = borrow.getBorrowDate().toLocalDate().plusDays(7);
            borrow.setExpectedReturnDate(Date.valueOf(expected));
        }
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, borrow.getUserId());
            pst.setDate(2, borrow.getBorrowDate());
            pst.setDate(3, borrow.getExpectedReturnDate());   
            pst.setString(4, borrow.getStatus());
            if (pst.executeUpdate() > 0) {
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    borrowId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return borrowId;
    }

    public boolean addBorrowDetail(BorrowDetailDTO detail) {
        String sql = InsertBorrowDetail;
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, detail.getBorrowId());
            pst.setInt(2, detail.getBookId());
            pst.setInt(3, detail.getQuantity());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean markReturned(int borrowId, Date returnDate) {
        String sql = UpdateReturn;
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setDate(1, returnDate);
            pst.setString(2, "Returned");
            pst.setInt(3, borrowId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void updateStatus(int borrowId, String newStatus) {
    try (Connection con = DBUtils.getConnection();
        PreparedStatement ps = con.prepareStatement(UpdateStatus)) {

        ps.setString(1, newStatus);
        ps.setInt(2, borrowId);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    //phong them
    public BorrowDTO getBorrowById(int borrowId) throws Exception {
    String sql = "SELECT b.BorrowID, b.UserID, u.FullName, b.BorrowDate, b.ExpectedReturnDate, " +
                 "b.ReturnDate, b.Status " +
                 "FROM Borrows b JOIN Users u ON b.UserID = u.UserID " +
                 "WHERE b.BorrowID = ?";

    try (Connection con = DBUtils.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, borrowId);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                BorrowDTO dto = new BorrowDTO();
                dto.setBorrowId(rs.getInt("BorrowID"));
                dto.setUserId(rs.getInt("UserID"));
                dto.setFullName(rs.getString("FullName"));
                dto.setBorrowDate(rs.getDate("BorrowDate"));
                dto.setExpectedReturnDate(rs.getDate("ExpectedReturnDate"));
                dto.setReturnDate(rs.getDate("ReturnDate"));
                dto.setStatus(rs.getString("Status"));  // ✅ sử dụng đúng tên cột
                return dto;
            }
        }
    }
    return null;
}


}
