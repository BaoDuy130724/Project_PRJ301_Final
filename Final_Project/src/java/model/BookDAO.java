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

/**
 *
 * @author Admin
 */
public class BookDAO {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static final String GetBook = "SELECT * FROM Books ";
    private static final String UpdateBook = "UPDATE Books SET......";
    private static final String CreateBook = "INSERT INTO Books (Title, Author, Publisher, YearPublished, ISBN, CategoryId, Quantity, Available) "
                                            +"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public List<BookDTO> getBooksByISBN (String ISBN_input){
        List<BookDTO> books = new ArrayList<>();
        String sql = GetBook + "WHERE ISBN like ?";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, "%"+ISBN_input.trim()+"%");
            rs = pst.executeQuery();
            while (rs.next()){
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = ISBN_input;
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                books.add(new BookDTO(bookId, title, author, publisher, bookId, ISBN, categoryID, quantity, available));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
    public List<BookDTO> getBooksByTitle (String title_input){
        List<BookDTO> books = new ArrayList<>();
        String sql = GetBook + "WHERE Title like ?";
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, "%"+title_input.trim()+"%");
            rs = pst.executeQuery();
            while (rs.next()){
                int bookId = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                int yearPublished = rs.getInt("YearPublished");
                String ISBN = rs.getString("ISBN");
                int categoryID = rs.getInt("CategoryID");
                int quantity = rs.getInt("Quantity");
                int available = rs.getInt("Available");
                books.add(new BookDTO(bookId, title, author, publisher, bookId, ISBN, categoryID, quantity, available));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
    public boolean createBook (BookDTO book){
        if(checkISBN(book.getISBN())) return false;
        String sql = CreateBook;
        try {
            con = utils.DBUtils.getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getPublisher());
            pst.setInt(4, book.getYear());
            pst.setString(5, book.getISBN());
            pst.setInt(6, book.getCategoryId());
            pst.setInt(7, book.getQuantity());
            pst.setInt(8, book.getAvailable()); 
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean checkISBN (String ISBN){
        List<BookDTO> checkedBook = getBooksByISBN(ISBN.trim());
        return checkedBook != null && !checkedBook.isEmpty(); 
    }
}
