/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ReservationDTO {
    private int reservationID;
    private int userID;
    private String fullName;
    private Date reserveDate;   
    private String statusCode;
    private Date deadlineToPickup; 
    private String statusDisplayName;
    private List<ReservationDetailDTO> details;

    public ReservationDTO() {
    }

    public ReservationDTO(int reservationID, int userID, String fullName, Date reserveDate, String statusCode, Date deadlineToPickup, String statusDisplayName, List<ReservationDetailDTO> details) {
        this.reservationID = reservationID;
        this.userID = userID;
        this.fullName = fullName;
        this.reserveDate = reserveDate;
        this.statusCode = statusCode;
        this.deadlineToPickup = deadlineToPickup;
        this.statusDisplayName = statusDisplayName;
        this.details = details;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getDeadlineToPickup() {
        return deadlineToPickup;
    }

    public void setDeadlineToPickup(Date deadlineToPickup) {
        this.deadlineToPickup = deadlineToPickup;
    }

    public String getStatusDisplayName() {
        return statusDisplayName;
    }

    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }

    public List<ReservationDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<ReservationDetailDTO> details) {
        this.details = details;
    }

    
    
}
