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
public class BorrowDTO {
    private int borrowId;
    private int userId;
    private String fullName;
    private Date borrowDate;
    private Date expectedReturnDate;
    private Date returnDate;
    private String status;
    public BorrowDTO(){
        
    }

    public BorrowDTO(int borrowId, int userId, String fullName, Date borrowDate, Date expectedReturnDate, Date returnDate, String status) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.fullName = fullName;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
        this.status = status;
    }
    

    public int getBorrowId() {
        return borrowId;
    }
    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getUserId() {
        return userId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Date getExpectedReturnDate() {
    return expectedReturnDate;
}

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }
}
