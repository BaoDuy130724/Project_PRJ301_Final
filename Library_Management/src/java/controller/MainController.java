/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,   
    maxFileSize = 1024 * 1024 * 10,      
    maxRequestSize = 1024 * 1024 * 15      
)
public class MainController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static String WELCOME = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME;
        String action = request.getParameter("action");
        try {
            if (action != null) {
                if (isUserAction(action)) {
                    url = "UserController";
                } else if (isBookAction(action)) {
                    url = "BookController";
                } else if (isBorrowAction(action)) {
                    url = "BorrowController";
                }else if (isReservationAction(action)) {
                    url = "ReservationController";
                }else if (isFineAction(action)) {    
                    url = "FineController";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean isUserAction(String action) {
        List<String> listUserAction = new ArrayList<>();
        listUserAction.add("login");
        listUserAction.add("register");
        listUserAction.add("home");
        listUserAction.add("logout");
        listUserAction.add("viewProfile");
        listUserAction.add("updateProfile");
        if (listUserAction.contains(action)) {
            return true;
        }
        return false;
    }

    private boolean isBookAction(String action) {
        List<String> listBookAction = new ArrayList<>();
        listBookAction.add("search");
        listBookAction.add("addBook");
        listBookAction.add("bookSubmitting");
        listBookAction.add("editBook");
        listBookAction.add("deleteBook");
        listBookAction.add("preOrder"); 
        if (listBookAction.contains(action)) {
            return true;
        }
        return false;
    }
    private boolean isBorrowAction(String action) {
        List<String> listBorrowAction = new ArrayList<>();
        listBorrowAction.add("viewAllBorrows");
        listBorrowAction.add("viewBorrowDetailAjax");
        listBorrowAction.add("searchMyBorrows");
        listBorrowAction.add("addToCart");
        listBorrowAction.add("confirmBorrow");
        listBorrowAction.add("removeFromCart");
        listBorrowAction.add("markReturned");
        listBorrowAction.add("markLostForm");
        listBorrowAction.add("confirmMarkLost");
        if (listBorrowAction.contains(action)) {
            return true;
        }
        return false;
    }
    
    private boolean isReservationAction(String action) {
        List<String> list = new ArrayList<>();
        list.add("viewReservations");     
        list.add("addToReservationCart"); 
        list.add("viewReservationCart"); 
        list.add("removeFromReservationCart"); 
        list.add("clearReservationCart"); 
        list.add("confirmReservation");  
        list.add("updateQuantity");
        list.add("viewReservationHistory");
        list.add("viewReservationDetail");
        list.add("cancelReservation");
        list.add("fulfillReservation");
        list.add("adminViewAllReservations");
        list.add("searchReservationsForAdmin");
        return list.contains(action);
    }
private boolean isFineAction(String action) {
    List<String> listFineAction = new ArrayList<>();
    listFineAction.add("listFine");
    listFineAction.add("createForm");
    listFineAction.add("createFine");
    listFineAction.add("updateStatus");
    return listFineAction.contains(action);
}


}
