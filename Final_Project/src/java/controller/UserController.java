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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.UserDAO;
import model.UserDTO;
import utils.*;

/**
 *
 * @author Admin
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {
    private static String WELCOME = "welcome.jsp";
    private static String LOGIN_PAGE = "login.jsp";
    UserDAO uDAO = new UserDAO();
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
        String url = LOGIN_PAGE;
        try {
            String action = request.getParameter("action");
            if(action.equals("login")){
                url = handleLogin(request, response);
            } else if (action.equals("logout")){
                url = handleLogout(request, response);
            }else if (action.equals("register")){
                url = handleRegister(request, response);
            }else if (action.equals("updateProfile")){
                url = handleLogout(request, response);
            }
        } catch (Exception e) {
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

    private String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String url = LOGIN_PAGE;
        HttpSession session = request.getSession();
        try {
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            password = PasswordUtils.encryptSHA256(password);
            boolean logined = uDAO.checkLogin(name, password);
            if(logined){
                url = WELCOME;
                UserDTO user = uDAO.getUserByName(name);
                GeneralMethod.prepareDashboard(request);
                session.setAttribute("user", user);
            } else {
                url = LOGIN_PAGE;
                request.setAttribute("message", "User name or Password incorrect!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = request.getSession(false);
        if(s!=null){
            s.invalidate();
        }
        return LOGIN_PAGE;
    } 

    private String handleRegister(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
