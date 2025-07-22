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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.BookDAO;
import model.BookDTO;
import model.BorrowDAO;
import model.BorrowDTO;
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
    BookDAO bdao = new BookDAO();
    BorrowDAO brdao = new BorrowDAO();

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
            if (action.equals("login")) {
                url = handleLogin(request, response);
            } else if (action.equals("logout")) {
                url = handleLogout(request, response);
            } else if (action.equals("home")) {
                url = handleHome(request, response);
            } else if (action.equals("register")) {
                url = handleRegister(request, response);
            } else if (action.equals("viewProfile")) {
                url = handleViewProfile(request, response);
            } else if (action.equals("updateProfile")) {
                url = handleUserUpdating(request, response);
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

    private String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String url = LOGIN_PAGE;
        HttpSession session = request.getSession();
        List<BookDTO> books = new ArrayList<>();
        try {
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            password = PasswordUtils.encryptSHA256(password);
            boolean logined = uDAO.checkLogin(name, password);
            if (logined) {
                url = WELCOME;
                UserDTO user = uDAO.getUserByName(name);
                GeneralMethod.prepareDashboard(request, user.getRole());
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
        if (s != null) {
            s.invalidate();
        }
        return LOGIN_PAGE;
    }

    private String handleRegister(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (username == null || username.isEmpty() || fullName == null || fullName.isEmpty()
                || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("userName", username);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("error", "Please fill all fields of register form!!!!");
            return "register.jsp";
        }
        try {
            String code = UUID.randomUUID().toString();
            password = PasswordUtils.encryptSHA256(password);
            if (uDAO.isEmailExist(email)) {
                request.setAttribute("userName", username);
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("error", "Email is already registered!");
            } else {
                UserDTO newUser = new UserDTO(username, password, fullName, email, "member", false, code);
                boolean result = uDAO.insertUser(newUser);
                if (result) {
                    System.out.println("Sending verification email to: " + email);
                    EmailUtils.sendVerificationEmail(email, fullName, code);
                    System.out.println("Email sent successfully!");
                    request.setAttribute("success", "Registration successful! Please check your email to verify your account.");
                } else {
                    request.setAttribute("userName", username);
                    request.setAttribute("fullName", fullName);
                    request.setAttribute("email", email);
                    request.setAttribute("error", "Registration failed!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error sending email: " + e.getMessage());
        }
        return "register.jsp";
    }

    private String handleViewProfile(HttpServletRequest request, HttpServletResponse response) {
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "profile";
        }

        UserDTO user = GeneralMethod.getCurrentUser(request);
        List<BorrowDTO> list = brdao.getBorrowsByUser(user.getUserID());
        request.setAttribute("myBorrows", list);

        request.setAttribute("activeTab", tab);
        request.setAttribute("user", user);
        return "profile.jsp";
    }

    private String handleUserUpdating(HttpServletRequest request, HttpServletResponse response) {
        String url = "profile.jsp";
        try {
            UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
            if (currentUser == null) {
                request.setAttribute("message", "You must be logged in to update profile.");
                return "login.jsp";
            }

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            if (fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                request.setAttribute("message", "Full name and email must not be empty.");
                return url;
            }

            currentUser.setFullName(fullName.trim());
            currentUser.setEmail(email.trim());

            boolean success = uDAO.updateProfile(currentUser);
            if (success) {
                request.setAttribute("message", "Profile updated successfully.");
                request.getSession().setAttribute("user", currentUser);
            } else {
                request.setAttribute("message", "Failed to update profile.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "An error occurred while updating profile.");
        }
        return url;
    }

    private String handleHome(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = GeneralMethod.getCurrentUser(request);
        GeneralMethod.prepareDashboard(request,user.getRole());
        return WELCOME;
    }
}
