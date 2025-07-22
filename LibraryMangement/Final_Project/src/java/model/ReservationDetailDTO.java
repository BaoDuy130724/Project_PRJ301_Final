/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class ReservationDetailDTO {
    private int reservationID;
    private int bookID;
    private String statusCode;
    private String bookTitle;
    private String statusDisplayName;
    private String statusDescription;
    private int quantity;

    public ReservationDetailDTO() {
    }

    public ReservationDetailDTO(int reservationID, int bookID, String statusCode, String bookTitle, String statusDisplayName, String statusDescription, int quantity) {
        this.reservationID = reservationID;
        this.bookID = bookID;
        this.statusCode = statusCode;
        this.bookTitle = bookTitle;
        this.statusDisplayName = statusDisplayName;
        this.statusDescription = statusDescription;
        this.quantity = quantity;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
}
