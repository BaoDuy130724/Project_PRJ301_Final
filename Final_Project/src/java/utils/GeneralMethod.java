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
import model.BorrowDAO;
import model.BorrowDTO;
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
    public static void getAccessDenied (HttpServletRequest request, String message){
        request.setAttribute("accessDenied", message);
    }
    static BookDAO bdao = new BookDAO();
    static CategoryDAO cdao = new CategoryDAO();
    static BorrowDAO brdao = new BorrowDAO();
    public static void pushListBook(HttpServletRequest request, String role){
        List<BookDTO> books = new ArrayList<>();
        if(role.equalsIgnoreCase("admin")){
            books = bdao.getAllBooks();
        }else if(role.equalsIgnoreCase("member")){
            books = bdao.getActiveBooks();
        }
        request.setAttribute("listBooks", books);
    }
    public static void pushListCategory(HttpServletRequest request){
        List<CategoryDTO> categories = new ArrayList<>();
        categories = cdao.getAllCategories();
        request.setAttribute("listCategories", categories);
    }
    public static void prepareDashboard(HttpServletRequest request, String role){
        pushListBook(request,role);
        pushListCategory(request);
    }
    public static void pushListBorrow(HttpServletRequest request){
        List<BorrowDTO> borrows = new ArrayList<>();
        borrows = brdao.getAllBorrows();
        request.setAttribute("listBorrows", borrows);
    }
}
