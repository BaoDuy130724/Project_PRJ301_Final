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
public class FineDTO {
    private int fineID;
    private int borrowID;
    private double amount;
    private String reason;
    private String statusCode;
    private Date createdAt;
    private String statusDisplayName;
    private String statusDescription;
    private String reasonDisplayName;
    private String reasonDescription;
    private String fullName;

    public FineDTO() {
    }

    public FineDTO(int fineID, int borrowID, double amount, String reason, String statusCode, Date createdAt, String statusDisplayName, String statusDescription, String reasonDisplayName, String reasonDescription, String fullName) {
        this.fineID = fineID;
        this.borrowID = borrowID;
        this.amount = amount;
        this.reason = reason;
        this.statusCode = statusCode;
        this.createdAt = createdAt;
        this.statusDisplayName = statusDisplayName;
        this.statusDescription = statusDescription;
        this.reasonDisplayName = reasonDisplayName;
        this.reasonDescription = reasonDescription;
        this.fullName = fullName;
    }

    public int getFineID() {
        return fineID;
    }

    public void setFineID(int fineID) {
        this.fineID = fineID;
    }

    public int getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(int borrowID) {
        this.borrowID = borrowID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatusDisplayName() {
        return statusDisplayName;
    }

    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getReasonDisplayName() {
        return reasonDisplayName;
    }

    public void setReasonDisplayName(String reasonDisplayName) {
        this.reasonDisplayName = reasonDisplayName;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    
}
