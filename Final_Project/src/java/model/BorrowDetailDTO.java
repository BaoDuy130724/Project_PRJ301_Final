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
    private int quantity;

    public BorrowDetailDTO() {
    }

    public BorrowDetailDTO(int borrowId, int bookId, int quantity) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
