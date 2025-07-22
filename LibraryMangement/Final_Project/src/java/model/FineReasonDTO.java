/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class FineReasonDTO {
    private String reasonCode;
    private String displayName;
    private String description;

    public FineReasonDTO() {
    }

    public FineReasonDTO(String reasonCode, String displayName) {
        this.reasonCode = reasonCode;
        this.displayName = displayName;
    }

    public FineReasonDTO(String reasonCode, String displayName, String description) {
        this.reasonCode = reasonCode;
        this.displayName = displayName;
        this.description = description;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
