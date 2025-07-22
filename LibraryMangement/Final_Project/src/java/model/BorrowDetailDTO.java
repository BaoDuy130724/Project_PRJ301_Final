/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class BorrowDetailDTO {
    private int borrowId;
    private int bookId;
    private String bookTitle;
    private int quantity;
    private int LostQty;

    public BorrowDetailDTO() {
    }

    public BorrowDetailDTO(int borrowId, int bookId, String bookTitle, int quantity, int LostQty) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.LostQty = LostQty;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLostQty() {
        return LostQty;
    }

    public void setLostQty(int LostQty) {
        this.LostQty = LostQty;
    }

    
}
