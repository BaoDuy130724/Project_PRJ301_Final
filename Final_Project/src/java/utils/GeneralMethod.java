/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.BookDAO;
import model.BookDTO;
import model.CategoryDAO;
import model.CategoryDTO;
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
    static BookDAO bdao = new BookDAO();
    static CategoryDAO cdao = new CategoryDAO();
    public static void pushListBook(HttpServletRequest request){
        List<BookDTO> books = new ArrayList<>();
        books = bdao.getAllBooks();
        request.setAttribute("listBooks", books);
    }
    public static void pushListCategory(HttpServletRequest request){
        List<CategoryDTO> categories = new ArrayList<>();
        categories = cdao.getAllCategories();
        request.setAttribute("listCategories", categories);
    }
    public static void prepareDashboard(HttpServletRequest request){
        pushListBook(request);
        pushListCategory(request);
    }
}
