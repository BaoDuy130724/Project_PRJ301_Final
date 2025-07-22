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
import utils.*;

/**
 *
 * @author Admin
 */
public class BookDAO {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static final String GetBook = "SELECT * FROM Books ";
    private static final String UpdateBook = "UPDATE Books SET Title=?, Author=?, Publisher=?, YearPublished=?, "
            + "ISBN=?, CategoryID=?, Quantity=?, Available=?, image=? WHERE BookID=?";
    private static final String CreateBook = "INSERT INTO Books (Title, Author, Publisher, YearPublished, ISBN, CategoryId, Quantity, Available, image) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DeleteBook = "UPDATE Books SET IsDeleted = 1 WHERE bookId = ?";
    
    private static final String UpdateAvailable = "UPDATE Books SET Available = Available + ? WHERE BookID = ?";
    public List<BookDTO> getAllBooks() {
        List<BookDTO> books = new ArrayList<>();
        String sql = GetBook;
        try {
            con = DBUtils.getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = rs.getString("ISBN");
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                boolean isdeleted = rs.getBoolean("IsDeleted");
                String image = rs.getString("image");
                books.add(new BookDTO(bookId, title, author, publisher, yearPublished, ISBN, categoryID, quantity, available, isdeleted, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<BookDTO> getActiveBooks() {
        List<BookDTO> books = new ArrayList<>();
        String sql = GetBook + "WHERE IsDeleted = 0";
        try {
            con = DBUtils.getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = rs.getString("ISBN");
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                boolean isdeleted = rs.getBoolean("IsDeleted");
                String image = rs.getString("image");
                books.add(new BookDTO(bookId, title, author, publisher, yearPublished, ISBN, categoryID, quantity, available, isdeleted, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<BookDTO> getBooksByISBN(String ISBN_input) {
        List<BookDTO> books = new ArrayList<>();
        String sql = GetBook + "WHERE ISBN like ? AND IsDeleted = 0";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, "%" + ISBN_input.trim() + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = rs.getString("ISBN");
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                boolean isdeleted = rs.getBoolean("IsDeleted");
                String image = rs.getString("image");
                books.add(new BookDTO(bookId, title, author, publisher, yearPublished, ISBN, categoryID, quantity, available, isdeleted, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<BookDTO> getBooksByTitle(String title_input) {
        List<BookDTO> books = new ArrayList<>();
        String sql = GetBook + "WHERE Title like ? AND IsDeleted = 0";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, "%" + title_input.trim() + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = rs.getString("ISBN");
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                boolean isdeleted = rs.getBoolean("IsDeleted");
                String image = rs.getString("image");
                books.add(new BookDTO(bookId, title, author, publisher, yearPublished, ISBN, categoryID, quantity, available, isdeleted, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public BookDTO getBookById(int id) {
        BookDTO book = new BookDTO();
        String sql = GetBook + "WHERE bookId = ? AND IsDeleted = 0";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = rs.getString("ISBN");
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                boolean isdeleted = rs.getBoolean("IsDeleted");
                String image = rs.getString("image");
                book = new BookDTO(bookId, title, author, publisher, yearPublished, ISBN, categoryID, quantity, available, isdeleted, image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    public boolean createBook(BookDTO book) {
        if (checkISBN(book.getISBN())){
            System.out.println(">> Duplicate ISBN: " + book.getISBN());
            return false;
        }
        String sql = CreateBook;
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getPublisher());
            pst.setInt(4, book.getYearPublished());
            pst.setString(5, book.getISBN());
            pst.setInt(6, book.getCategoryId());
            pst.setInt(7, book.getQuantity());
            pst.setInt(8, book.getAvailable());
            pst.setString(9, book.getImage());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkISBN(String ISBN) {
        List<BookDTO> checkedBook = getBooksByISBN(ISBN.trim());
        return checkedBook != null && !checkedBook.isEmpty();
    }

    public boolean deleteBook(int id) {
        String sql = DeleteBook;
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBook(BookDTO book) {
        String sql = UpdateBook;
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getPublisher());
            pst.setInt(4, book.getYearPublished());
            pst.setString(5, book.getISBN());
            pst.setInt(6, book.getCategoryId());
            pst.setInt(7, book.getQuantity());
            pst.setInt(8, book.getAvailable());
            pst.setString(9, book.getImage());
            pst.setInt(10, book.getBookId());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateBookAvailable(int bookId, int delta){
        String sql = "UPDATE Books SET Available = Available + ? WHERE BookID = ?";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, delta);
            pst.setInt(2, bookId);
            return pst.executeUpdate()>0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getLastInsertedBookId() {
        String sql = "SELECT MAX(bookId) FROM Books";
        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean updateBookImage(int bookId, String fileName){
        String sql = "UPDATE Books SET image = ? WHERE BookId = ?";
        try{
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, fileName);
            pst.setInt(2, bookId);
            return pst.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean increaseAvailable(int bookid, int quantity){
        String sql = UpdateAvailable;
        try{
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, quantity);
            pst.setInt(2, bookid);
            return pst.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //phong them
    public List<BookDTO> getUnavailableBooks() {
        List<BookDTO> books = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE Available = 0 AND IsDeleted = 0";

        try {
            con = DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = rs.getString("ISBN");
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                boolean isDeleted = rs.getBoolean("IsDeleted");
                String image = rs.getString("image");

                books.add(new BookDTO(bookId, title, author, publisher, yearPublished, ISBN, categoryID, quantity, available, isDeleted, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return books;
    }
    public boolean decreaseBookQuantity(int bookId, int amount) {
        String sql = "UPDATE Books SET Quantity = Quantity - ? WHERE BookID = ? AND Quantity >= ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, amount);  // giảm Quantity
            pst.setInt(2, bookId);
            pst.setInt(3, amount);  // tránh Quantity âm

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
