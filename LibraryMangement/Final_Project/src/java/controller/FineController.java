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
import java.sql.Date;
import java.util.List;
import model.FineDAO;
import model.FineDTO;
import model.FineReasonDAO;
import model.FineReasonDTO;
import model.UserDTO;
import utils.GeneralMethod;


/**
 *
 * @author Admin
 */
@WebServlet(name = "FineController", urlPatterns = {"/FineController"})
public class FineController extends HttpServlet {
    FineDAO fineDAO = new FineDAO();
    FineReasonDAO reasonDAO = new FineReasonDAO();
    private static final String WELCOME = "welcome.jsp";
    private static final String LOGIN = "login.jsp";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String url = "fineList.jsp";

        try {
            if (action == null || action.equals("listFine")) {
                url = handleListFines(request, response);
            } else if (action.equals("createForm")) {
                url = handleShowCreateForm(request, response);
            } else if (action.equals("createFine")) {
                url = handleCreateFine(request, response);
            } else if (action.equals("updateStatus")) {
                url = handleUpdateStatus(request, response);
            }else if (action.equals("searchFine")) {
                url = handleSearchFines(request, response);
            }    
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Internal error: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
    
    private String handleListFines(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!GeneralMethod.isLoggedIn(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to access this page");
            return LOGIN;
        }
        UserDTO user = GeneralMethod.getCurrentUser(request);
        List<FineDTO> fines;

        if (GeneralMethod.isAdmin(request)) {
            fines = fineDAO.getAllFines();
        } else {
            fines = fineDAO.getFinesByUserID(user.getUserID());
        }

        request.setAttribute("fineList", fines);
        return "fineList.jsp";
    }

    private String handleShowCreateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to access this page");
            return WELCOME;
        }

        List<FineReasonDTO> reasons = reasonDAO.getAllReasons();
        request.setAttribute("reasonList", reasons);

        // Lấy borrowId truyền từ request
        String borrowIdStr = request.getParameter("borrowId");
        if (borrowIdStr != null) {
            int borrowId = Integer.parseInt(borrowIdStr);
            request.setAttribute("borrowId", borrowId);
        }

        return "createFine.jsp";
    }


    private String handleCreateFine(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to perform this action");
            return WELCOME;
        }

        try {
            int borrowId = Integer.parseInt(request.getParameter("borrowId"));
            String reason = request.getParameter("reason");
            double amount = Double.parseDouble(request.getParameter("amount"));

            if (reason == null || reason.trim().isEmpty() || amount <= 0) {
                request.setAttribute("message", "Invalid fine data.");
                return handleShowCreateForm(request, response); // Quay lại form
            }

            FineDTO fine = new FineDTO();
            fine.setBorrowID(borrowId);
            fine.setReason(reason);
            fine.setAmount(amount);
            fine.setStatusCode("Unpaid");
            fine.setCreatedAt(new Date(System.currentTimeMillis()));

            boolean inserted = fineDAO.insertFine(fine);
            request.setAttribute("message", inserted ? "Fine created successfully." : "Failed to create fine.");

        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid input.");
        }

        return handleListFines(request, response);
    }
    
    private String handleUpdateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to update fine status");
            return WELCOME;
        }

        int fineId = Integer.parseInt(request.getParameter("fineId"));
        String newStatus = request.getParameter("status");
        boolean updated = fineDAO.updateStatus(fineId, newStatus);

        request.setAttribute("message", updated ? "Status updated successfully." : "Failed to update status.");
        return handleListFines(request, response);
    }
    
    private String handleSearchFines(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!GeneralMethod.isLoggedIn(request)) {
            GeneralMethod.getAccessDenied(request, "You must login to use this feature.");
            return LOGIN;
        }

        String reason = request.getParameter("reason");
        String status = request.getParameter("status");
        String name = request.getParameter("name");

        List<FineDTO> results;
        if (GeneralMethod.isAdmin(request)) {
            results = fineDAO.searchFines(reason, status, name);
        } else {
            UserDTO user = GeneralMethod.getCurrentUser(request);
            results = fineDAO.searchFinesByUserID(user.getUserID(), reason, status);
        }

        request.setAttribute("fineList", results);
        return "fineList.jsp";
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

}
