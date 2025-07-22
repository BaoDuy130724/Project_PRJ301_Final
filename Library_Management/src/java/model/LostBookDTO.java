/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class LostBookDTO {
    private int lostId;
    private int borrowId;
    private int bookId;
    private int quantity;
    private Date createdAt;

    public LostBookDTO() {
    }

    public LostBookDTO(int lostId, int borrowId, int bookId, int quantity, Date createdAt) {
        this.lostId = lostId;
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }
    
    public int getLostId() {
        return lostId;
    }

    public void setLostId(int lostId) {
        this.lostId = lostId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    
}
