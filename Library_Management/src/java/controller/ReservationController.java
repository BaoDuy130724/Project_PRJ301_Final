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
import model.BookDAO;
import model.BookDTO;
import model.CartItemDTO;
import model.ReservationDAO;
import model.ReservationDTO;
import model.ReservationStatusDAO;
import model.ReservationStatusDTO;
import model.UserDTO;
import utils.GeneralMethod;


/**
 *
 * @author Admin
 */
@WebServlet(name = "ReservationController", urlPatterns = {"/ReservationController"})
public class ReservationController extends HttpServlet {
    private static final String VIEW_PAGE = "reservationSelect.jsp";
    private static final String CART_PAGE = "reservationCart.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String WELCOME = "welcome.jsp";
    BookDAO bookDAO = new BookDAO();
    ReservationDAO reservationDAO = new ReservationDAO();

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
            String url;

            try {
                if (action == null || action.equals("viewReservations")) {
                    url = handleViewReservations(request);
                } else if (action.equals("addToReservationCart")) {
                    url = handleAddToReservationCart(request);
                } else if (action.equals("viewReservationCart")) {
                    url = CART_PAGE;
                } else if (action.equals("removeFromReservationCart")) {
                    url = handleRemoveFromReservationCart(request);
                } else if (action.equals("clearReservationCart")) {
                    url = handleClearReservationCart(request);
                } else if (action.equals("confirmReservation")) {
                    url = handleConfirmReservation(request);
                }else if (action.equals("updateQuantity")) {
                    url = handleUpdateQuantity(request);
                }else if (action.equals("viewReservationHistory")) {
                    url = handleViewReservationHistory(request);
                }else if (action.equals("viewReservationDetail")) {
                    url = handleViewReservationDetail(request);
                }else if (action.equals("cancelReservation")) {
                    url = handleCancelReservation(request);
                }else if (action.equals("fulfillReservation")) {
                    url = handleFulfillReservation(request);
                }else if (action.equals("adminViewAllReservations")) {
                    url = handleAdminViewAllReservations(request);
                }else if (action.equals("searchReservationsForAdmin")) {
                    url = handleSearchReservationsForAdmin(request);
                }
                else {
                    request.setAttribute("message", "Invalid action.");
                    url = VIEW_PAGE;
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("message", "Error: " + e.getMessage());
                url = VIEW_PAGE;
            }

            request.getRequestDispatcher(url).forward(request, response);
    }
    private String handleViewReservations(HttpServletRequest request) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        List<BookDTO> unavailableBooks = bookDAO.getUnavailableBooks();
        request.setAttribute("unavailableBooks", unavailableBooks);
        return VIEW_PAGE;
    }

    private String handleAddToReservationCart(HttpServletRequest request) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        HttpSession session = request.getSession();
        List<CartItemDTO> cart = getOrCreateCart(session);

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int quantityToAdd = Integer.parseInt(request.getParameter("quantity"));
        BookDTO book = bookDAO.getBookById(bookId);

        if (book == null) {
            request.setAttribute("message", "Book not found.");
            return VIEW_PAGE;
        }

        boolean found = false;
        for (CartItemDTO item : cart) {
            if (item.getBook().getBookId() == bookId) {
                item.setQuantity(item.getQuantity() + quantityToAdd);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItemDTO(book, quantityToAdd));
        }

        session.setAttribute("reservationCart", cart);
        request.setAttribute("message", "Book added to reservation cart.");
        return "MainController?action=viewReservations"; 
    }

    private String handleRemoveFromReservationCart(HttpServletRequest request) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        HttpSession session = request.getSession();
        List<CartItemDTO> cart = getOrCreateCart(session);

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        cart.removeIf(item -> item.getBook().getBookId() == bookId);

        session.setAttribute("reservationCart", cart);
        request.setAttribute("message", "Book removed from cart.");
        return CART_PAGE;
    }

    private String handleClearReservationCart(HttpServletRequest request) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        HttpSession session = request.getSession();
        List<CartItemDTO> cart = getOrCreateCart(session);
        cart.clear();
        session.setAttribute("reservationCart", cart);
        request.setAttribute("message", "Reservation cart cleared.");
        return CART_PAGE;
    }

    private String handleConfirmReservation(HttpServletRequest request) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            request.setAttribute("message", "Please log in to confirm reservation.");
            return LOGIN_PAGE;
        }

        List<CartItemDTO> cart = getOrCreateCart(session);
        if (cart.isEmpty()) {
            request.setAttribute("message", "Cart is empty. Nothing to confirm.");
            return CART_PAGE;
        }

        boolean allSuccess = true;
        for (CartItemDTO item : cart) {
            boolean saved = reservationDAO.createReservationWithQuantity(
                user.getUserID(),
                item.getBook().getBookId(),
                item.getQuantity()
            );
            if (!saved) allSuccess = false;
        }

        // Clear cart regardless of success (per your requirement)
        cart.clear();
        session.setAttribute("reservationCart", cart);

        if (allSuccess) {
            request.setAttribute("message", "Reservation confirmed successfully.");
        } else {
            request.setAttribute("message", "Some reservations failed. Please check again.");
        }

        return CART_PAGE;
    }

    private String handleUpdateQuantity(HttpServletRequest request) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        HttpSession session = request.getSession();
        List<CartItemDTO> cart = getOrCreateCart(session);

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int newQuantity = Integer.parseInt(request.getParameter("quantity"));

        for (CartItemDTO item : cart) {
            if (item.getBook().getBookId() == bookId) {
                item.setQuantity(newQuantity);
                break;
            }
        }

        session.setAttribute("reservationCart", cart);
        request.setAttribute("message", "Quantity updated.");

        return CART_PAGE;
    }

    @SuppressWarnings("unchecked")
    private List<CartItemDTO> getOrCreateCart(HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("reservationCart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("reservationCart", cart);
        }
        return cart;
    }
    private String handleViewReservationHistory(HttpServletRequest request) {
        if (!GeneralMethod.isMember(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            request.setAttribute("message", "Please log in to view your reservation history.");
            return LOGIN_PAGE;
        }

        reservationDAO.autoExpireAllReadyToPickupReservations(); // ✅ Added
        reservationDAO.updateReservationsToReady();

        List<ReservationDTO> reservationList = reservationDAO.getReservationsByUserId(user.getUserID());
        request.setAttribute("reservationList", reservationList);
        return "reservationHistory.jsp";
    }




    private String handleViewReservationDetail(HttpServletRequest request) {
    if (!GeneralMethod.isLoggedIn(request)) {
        GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
        return WELCOME;
    }

    String ridStr = request.getParameter("reservationID");
    if (ridStr == null) {
        request.setAttribute("message", "Missing reservation ID.");
        return "reservationHistory.jsp";
    }

    try {
        int reservationID = Integer.parseInt(ridStr);

        // Update reservation status before showing detail
        reservationDAO.autoExpireAllReadyToPickupReservations(); // ✅ Expire outdated
        reservationDAO.updateReservationsToReady();              // ✅ Promote eligible

        ReservationDTO reservation = reservationDAO.getReservationById(reservationID);
        if (reservation == null) {
            request.setAttribute("message", "Reservation not found.");
            return "reservationHistory.jsp";
        }

        // ✅ Get status description
        ReservationStatusDAO statusDAO = new ReservationStatusDAO();
        ReservationStatusDTO statusDTO = statusDAO.getStatusByCode(reservation.getStatusCode());
        if (statusDTO != null) {
            request.setAttribute("statusDescription", statusDTO.getDescription());
        }

        request.setAttribute("reservation", reservation);
        return "reservationDetail.jsp";

    } catch (NumberFormatException e) {
        request.setAttribute("message", "Invalid reservation ID.");
        return "reservationHistory.jsp";
    }
}


    private String handleCancelReservation(HttpServletRequest request) {
        String ridStr = request.getParameter("reservationID");
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            request.setAttribute("message", "Please log in.");
            return LOGIN_PAGE;
        }

        try {
            int reservationID = Integer.parseInt(ridStr);
            boolean success = reservationDAO.cancelReservation(reservationID, user.getUserID());

            if (success) {
                request.setAttribute("message", "Reservation cancelled successfully.");
            } else {
                request.setAttribute("message", "Unable to cancel. It may have already been processed or not exist.");
            }
        } catch (Exception e) {
            request.setAttribute("message", "Error while canceling reservation: " + e.getMessage());
        }

        return handleViewReservationHistory(request); // Reload history page
    }

    private String handleFulfillReservation(HttpServletRequest request) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        String ridStr = request.getParameter("reservationID");

        if (ridStr == null) {
            request.setAttribute("message", "Missing reservation ID.");
            return "MainController?action=adminViewAllReservations";
        }

        try {
            int reservationID = Integer.parseInt(ridStr);
            reservationDAO.autoExpireAllReadyToPickupReservations();
            reservationDAO.updateReservationsToReady();

            boolean success = reservationDAO.fulfillReservation(reservationID);

            if (success) {
                request.setAttribute("message", "Reservation fulfilled successfully.");
            } else {
                request.setAttribute("message", "Cannot fulfill reservation. It may not be ready or books are unavailable.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid reservation ID.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error while fulfilling reservation: " + e.getMessage());
        }

        return "MainController?action=adminViewAllReservations";
    }



    private String handleAdminViewAllReservations(HttpServletRequest request) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }
        reservationDAO.autoExpireAllReadyToPickupReservations();
        reservationDAO.updateReservationsToReady();

        List<ReservationDTO> allReservations = reservationDAO.getAllReservations();
        request.setAttribute("reservationList", allReservations);
        return "reservationHistory.jsp";
    }
    private String handleSearchReservationsForAdmin(HttpServletRequest request) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to do this feature");
            return WELCOME;
        }

        reservationDAO.autoExpireAllReadyToPickupReservations();
        reservationDAO.updateReservationsToReady();

        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String fullName = request.getParameter("fullName");
        String status = request.getParameter("status");

        List<ReservationDTO> filteredList = reservationDAO.searchReservations(fromDate, toDate, fullName, status);
        request.setAttribute("reservationList", filteredList);

        // Giữ lại tham số tìm kiếm để hiển thị lại trên form
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.setAttribute("fullName", fullName);
        request.setAttribute("status", status);

        return "reservationHistory.jsp";
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
