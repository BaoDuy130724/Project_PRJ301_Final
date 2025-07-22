/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.BookDAO;
import model.BookDTO;
import model.BorrowDAO;
import model.BorrowDTO;
import model.BorrowDetailDTO;
import model.FineDAO;
import model.FineDTO;
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
            } else if (action.equals("markReturned")) {
                url = handleMarkReturned(request, response);
            }//phong them
            else if (action.equals("markLostForm")) {
                url = handleMarkLostForm(request, response);
            } else if (action.equals("confirmMarkLost")) {
                url = handleConfirmMarkLost(request, response);
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
                cart.add(new BorrowDetailDTO(0, bookId_value, book.getTitle(), 1, 0));
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
                //sua them ngay du kien
                java.time.LocalDate borrowLocalDate = currentDate.toLocalDate();
                java.sql.Date expectedReturnDate = java.sql.Date.valueOf(borrowLocalDate.plusDays(7));
                
                BorrowDTO borrow = new BorrowDTO(0, user.getUserID(), "", currentDate,expectedReturnDate, null, "Borrowing");
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
            String showCart = request.getParameter("showCart");
            if ("true".equals(showCart)) {
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

    private String handleMarkReturned(HttpServletRequest request, HttpServletResponse response) {
    if (!GeneralMethod.isAdmin(request)) {
        GeneralMethod.getAccessDenied(request, "You do not have permission to access this page");
        return WELCOME;
    }

    try {
        int borrowId = Integer.parseInt(request.getParameter("borrowId"));
        Date returnDate = new Date(System.currentTimeMillis());

        // Lấy chi tiết mượn để xử lý
        List<BorrowDetailDTO> detailList = brdao.getBorrowDetails(borrowId);
        int totalReturnable = 0;

        // Cập nhật số lượng sách được trả (trừ đi sách đã mất)
        for (BorrowDetailDTO detail : detailList) {
            int returnableQty = detail.getQuantity() - detail.getLostQty();
            if (returnableQty > 0) {
                totalReturnable += returnableQty;
                boolean updated = bdao.increaseAvailable(detail.getBookId(), returnableQty);
                if (!updated) {
                    System.out.println("Failed to update availability for book ID: " + detail.getBookId());
                }
            }
        }

        // Nếu không còn cuốn nào để trả (tất cả đều mất)
        if (totalReturnable == 0) {
            request.setAttribute("message", "Cannot return: all books in this borrow are marked as lost.");
            GeneralMethod.pushListBorrow(request);
            return "borrowList.jsp";
        }

        // Đánh dấu là đã trả
        boolean success = brdao.markReturned(borrowId, returnDate);
        if (!success) {
            request.setAttribute("message", "Failed to mark as returned.");
            GeneralMethod.pushListBorrow(request);
            return "borrowList.jsp";
        }

        // Kiểm tra trễ hạn để tính tiền phạt
        BorrowDTO borrow = brdao.getBorrowById(borrowId);
        if (borrow != null) {
            LocalDate expected = borrow.getExpectedReturnDate().toLocalDate();
            LocalDate actual = returnDate.toLocalDate();
            long daysLate = ChronoUnit.DAYS.between(expected, actual);

            if (daysLate > 0) {
                double fineAmount = daysLate * 5000;

                FineDTO fine = new FineDTO();
                fine.setBorrowID(borrowId);
                fine.setAmount(fineAmount);
                fine.setReason("OVERDUE");
                fine.setStatusCode("Unpaid");
                fine.setCreatedAt(returnDate);

                FineDAO fineDAO = new FineDAO();
                boolean fineInserted = fineDAO.insertFine(fine);
                if (fineInserted) {
                    request.setAttribute("message", "Marked borrow ID " + borrowId + " as returned (Late: " + daysLate + " days, fine: " + fineAmount + " VND).");
                } else {
                    request.setAttribute("message", "Returned, but failed to insert fine.");
                }
            } else {
                request.setAttribute("message", "Marked borrow ID " + borrowId + " as returned.");
            }
        } else {
            request.setAttribute("message", "Returned, but borrow data not found.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("message", "An error occurred while processing the return.");
    }

    GeneralMethod.pushListBorrow(request);
    return "borrowList.jsp";
}

    //phong them
    private String handleMarkLostForm(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "Access Denied");
            return WELCOME;
        }

        int borrowId = Integer.parseInt(request.getParameter("borrowId"));
        List<BorrowDetailDTO> details = brdao.getBorrowDetails(borrowId);

        request.setAttribute("borrowDetails", details);
        request.setAttribute("borrowId", borrowId);
        return "markLostForm.jsp";
    }

    private String handleConfirmMarkLost(HttpServletRequest request, HttpServletResponse response) {
    if (!GeneralMethod.isAdmin(request)) {
        GeneralMethod.getAccessDenied(request, "Access Denied");
        return WELCOME;
    }

    try {
        int borrowId = Integer.parseInt(request.getParameter("borrowId"));
        List<BorrowDetailDTO> details = brdao.getBorrowDetails(borrowId);

        double totalFine = 0;
        boolean hasLost = false;
        boolean allLost = true; // giả định ban đầu

        for (BorrowDetailDTO item : details) {
            int borrowedQty = item.getQuantity(); // tổng số sách đã mượn
            String paramName = "lostQty_" + item.getBookId();
            String value = request.getParameter(paramName);

            int lostQty = 0;
            if (value != null && !value.isEmpty()) {
                lostQty = Integer.parseInt(value);
            }

            if (lostQty > 0) {
                hasLost = true;
                totalFine += lostQty * 100_000;

                bdao.updateBookAvailable(item.getBookId(), -lostQty);
                brdao.updateLostQty(borrowId, item.getBookId(), lostQty);
            }

            if (lostQty < borrowedQty) {
                allLost = false; // còn sách chưa mất
            }
        }

        if (hasLost) {
            // Chỉ insert fine nếu có ít nhất 1 cuốn mất
            FineDTO fine = new FineDTO();
            fine.setBorrowID(borrowId);
            fine.setAmount(totalFine);
            fine.setReason("LOST");
            fine.setStatusCode("Unpaid");
            fine.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));

            FineDAO fineDAO = new FineDAO();
            boolean inserted = fineDAO.insertFine(fine);

            // Chỉ đánh dấu là Lost nếu mất hết tất cả sách
            if (allLost) {
                brdao.markAsLost(borrowId);
            }

            if (inserted) {
                request.setAttribute("message", "Marked lost books and fine created: " + totalFine + " VND");
            } else {
                request.setAttribute("message", "Failed to insert fine.");
            }
        } else {
            request.setAttribute("message", "No lost books selected.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("error", "An error occurred while marking lost books.");
    }

    GeneralMethod.pushListBorrow(request);
    return "borrowList.jsp";
}

}
