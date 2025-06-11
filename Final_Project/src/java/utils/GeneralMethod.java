/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;

/**
 *
 * @author Admin
 */
public class GeneralMethod {
    public static UserDTO getCurrentUser (HttpServletRequest request){
        HttpSession s = request.getSession();
        if(s!=null && s.getAttribute("user")!=null)
            return (UserDTO) s.getAttribute("user");
        return null;
    }
    public static boolean isLoggedIn (HttpServletRequest request){
        return getCurrentUser(request) != null;
    }
    public static boolean hasRole (HttpServletRequest request, String role){
        UserDTO user = getCurrentUser(request);
        if(user!=null){
            return user.getRole().equalsIgnoreCase(role.trim());
        }
        return false;
    }
    public static boolean isAdmin (HttpServletRequest request){
        return hasRole(request, "admin");
    }
    public static boolean isMember (HttpServletRequest request){
        return hasRole(request, "member");
    }
}
