/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class UserDTO {
    private int userID;
    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String role;
    private boolean status;
    private String verifyCode;
    public UserDTO (){
        
    }

    public UserDTO(int userID, String userName, String password, 
            String fullName, String email, String role, boolean status) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public UserDTO(String userName, String password, 
            String fullName, String email, String role, boolean status, String verifyCode) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.verifyCode = verifyCode;
    }
    

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
