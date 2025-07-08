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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import model.BookDAO;
import model.BookDTO;
import model.BorrowDAO;
import model.BorrowDTO;
import model.BorrowDetailDTO;
import model.UserDTO;
import org.apache.jasper.tagplugins.jstl.core.ForEach;
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
    BookDAO bdao = new BookDAO();

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
            } else if (action.equals("addToCart")) {
                url = handleAddToCart(request, response);
            } else if (action.equals("confirmBorrow")) {
                url = handleBorrowComfirmation(request, response);
            } else if (action.equals("removeFromCart")) {
                url = handleBorrowRemoving(request, response);
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

    private String handleAddToCart(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        try {
            HttpSession s = request.getSession();
            List<BorrowDetailDTO> cart = (List<BorrowDetailDTO>) s.getAttribute("borrowCart");
            if (cart == null) {
                cart = new ArrayList<>();
                s.setAttribute("borrowCart", cart);
            }
            String bookId = request.getParameter("bookId");
            if (bookId == null) {
                request.setAttribute("error", "Do not have this book");
                return WELCOME;
            }
            int bookId_value = Integer.parseInt(bookId);
            BookDTO book = bdao.getBookById(bookId_value);
            if (book == null || book.getAvailable() == 0) {
                request.setAttribute("error", "Book is not available");
                return WELCOME;
            }
            boolean found = false;
            for (BorrowDetailDTO item : cart) {
                if (item.getBookId() == bookId_value) {
                    item.setQuantity(item.getQuantity() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                cart.add(new BorrowDetailDTO(0, bookId_value, book.getTitle(), 1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        GeneralMethod.pushListBook(request, "member");
        return WELCOME;
    }

    private String handleBorrowComfirmation(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        try {
            HttpSession s = request.getSession(false);
            UserDTO user = (UserDTO) s.getAttribute("user");
            List<BorrowDetailDTO> cart = (List<BorrowDetailDTO>) s.getAttribute("borrowCart");
            if (user != null && cart != null && !cart.isEmpty()) {
                Date currentDate = new Date(System.currentTimeMillis());
                BorrowDTO borrow = new BorrowDTO(0, user.getUserID(), "", currentDate, null, "Borrowing");
                int borrowID = brdao.createBorrow(borrow);
                for (BorrowDetailDTO item : cart) {
                    item.setBorrowId(borrowID);
                    brdao.addBorrowDetail(item);
                    bdao.updateBookAvailable(item.getBookId(), -item.getQuantity());
                }
                s.removeAttribute("borrowCart");
                request.setAttribute("message", "Borrowing successful!");
            } else {
                request.setAttribute("error", "Your cart is empty.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GeneralMethod.pushListBook(request, "member");
        return WELCOME;
    }

    private String handleBorrowRemoving(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        try {
            String showCart =  request.getParameter("showCart");
            if("true".equals(showCart)){
                request.setAttribute("showCartPopup", true);
            }
            HttpSession s = request.getSession(false);
            String index = request.getParameter("index");
            if (index == null || index.isEmpty()) {
                request.setAttribute("error", "do not exist");
                return WELCOME;
            }
            int index_value = Integer.parseInt(index);
            List<BorrowDetailDTO> cart = (List<BorrowDetailDTO>) s.getAttribute("borrowCart");
            if (cart != null && index_value >= 0 && index_value < cart.size()) {
                cart.remove(index_value);
            }
        } catch (Exception e) {
        }
        GeneralMethod.pushListBook(request, "member");
        return WELCOME;
    }
}
