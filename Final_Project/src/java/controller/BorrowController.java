/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.BorrowDAO;
import model.BorrowDTO;
import model.UserDTO;
import utils.*;

/**
 *
 * @author Admin
 */
@WebServlet(name = "BorrowController", urlPatterns = {"/BorrowController"})
public class BorrowController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String WELCOME = "welcome.jsp";
    BorrowDAO brdao = new BorrowDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME;
        String action = request.getParameter("action");
        try {
            if (action.equals("viewAllBorrows")) {
                url = handleBorrowViewing(request, response);
            } else if (action.equals("viewBorrowDetailAjax")) {
                url = handleDetailViewing(request, response);
            } else if (action.equals("searchMyBorrows")) {
                url = handleBorrowSearching(request, response);
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

    private String handleBorrowViewing(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to access this page");
            return WELCOME;
        }
        String keyword = request.getParameter("txtSearch");
        List<BorrowDTO> borrows;
        if (keyword != null && !keyword.isEmpty()) {
            borrows = brdao.searchBorrowsByUserName(keyword.trim());
            request.setAttribute("searchName", keyword.trim());
            request.setAttribute("listBorrows", borrows);
        } else {
            GeneralMethod.pushListBorrow(request);
        }
        return "borrowList.jsp";
    }

    private String handleDetailViewing(HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession().getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        int id = Integer.parseInt(request.getParameter("borrowId"));
        request.setAttribute("details", brdao.getBorrowDetails(id));

        return "borrowDetailPartial.jsp";
    }

    private String handleBorrowSearching(HttpServletRequest request, HttpServletResponse response) {
        
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        UserDTO user = GeneralMethod.getCurrentUser(request);
        List<BorrowDTO> result = brdao.searchBorrowsByUserAndDate(user.getUserID(), fromDate, toDate);
        
        request.setAttribute("myBorrows", result);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate); 
        request.setAttribute("activeTab", "borrows");
        return "profile.jsp";
    }

}
