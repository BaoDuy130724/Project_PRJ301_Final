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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import model.BookDAO;
import model.BookDTO;
import model.BorrowDAO;
import model.BorrowDTO;
import model.BorrowDetailDTO;
import model.FineDAO;
import model.FineDTO;
import model.LostBookDAO;
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
            }else if (action.equals("confirmMarkLost")) {
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
            boolean success = brdao.markReturned(borrowId, returnDate);
            if (success) {
                List<BorrowDetailDTO> detailList = brdao.getBorrowDetails(borrowId);
                for(BorrowDetailDTO detail : detailList){
                    int bookid = detail.getBookId();
                    int quantity = detail.getQuantity();
                    boolean updated = bdao.increaseAvailable(bookid, quantity);
                    if(!updated){
                        System.out.println("Failed to update availability for book ID: " + bookid);
                    }
                }
            // phong them
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
        } else {
            request.setAttribute("message", "Failed to mark as returned.");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GeneralMethod.pushListBorrow(request);
        return "borrowList.jsp";
    }
    //phong them
    private String handleMarkLostForm(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to access this page");
            return WELCOME;
        }
        int borrowId = Integer.parseInt(request.getParameter("borrowId"));
        List<BorrowDetailDTO> details = brdao.getBorrowDetails(borrowId);
        request.setAttribute("borrowDetails", details);
        request.setAttribute("borrowId", borrowId);
        return "markLost.jsp";
    }
    private String handleConfirmMarkLost(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to access this page");
            return WELCOME;
        }

        try {
            int borrowId = Integer.parseInt(request.getParameter("borrowId"));
            List<BorrowDetailDTO> details = brdao.getBorrowDetails(borrowId);

            int totalLost = 0;
            int totalBooks = 0;
            double fineAmount = 0.0;
            final int finePerBook = 100000;

            FineDAO fineDAO = new FineDAO();
            LostBookDAO lostDAO = new LostBookDAO();

            for (BorrowDetailDTO item : details) {
                totalBooks += item.getQuantity(); // Tổng số sách được mượn

                String param = request.getParameter("lost_" + item.getBookId());
                int lostQty = 0;

                if (param != null && !param.isEmpty()) {
                    lostQty = Integer.parseInt(param);
                }

                if (lostQty > 0 && lostQty <= item.getQuantity()) {
                    // 🔻 Trừ tồn kho
                    bdao.updateBookAvailable(item.getBookId(), -lostQty);

                    // 🔻 Thêm vào bảng LostBooks
                    boolean lostInserted = lostDAO.insertLostBook(borrowId, item.getBookId(), lostQty);
                    if (!lostInserted) {
                        System.out.println("⚠ Failed to insert lost record for bookId: " + item.getBookId());
                    }

                    // 🔻 Tính tiền phạt
                    fineAmount += lostQty * finePerBook;
                    totalLost += lostQty;
                } else {
                    // ✅ Sách không bị mất → cộng lại toàn bộ vào Available
                    bdao.updateBookAvailable(item.getBookId(), item.getQuantity());
                }
            }

            // 4. Nếu có sách mất thì tạo fine
            if (totalLost > 0) {
                FineDTO fine = new FineDTO();
                fine.setBorrowID(borrowId);
                fine.setAmount(fineAmount);
                fine.setReason("LOST");
                fine.setStatusCode("Unpaid");
                fine.setCreatedAt(new Date(System.currentTimeMillis()));

                boolean fineCreated = fineDAO.insertFine(fine);
                if (fineCreated) {
                    String status;
                    if (totalLost == totalBooks) {
                        status = "Lost";
                    } else {
                        status = "Partially Lost";
                    }
                    brdao.updateStatus(borrowId, status);

                    request.setAttribute("message", "Marked as " + status + ". Fine: " + fineAmount + " VND for " + totalLost + " lost books.");
                } else {
                    request.setAttribute("message", "Lost books recorded, but failed to insert fine.");
                }
            } else {
                request.setAttribute("message", "No books were marked as lost.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error while processing lost books.");
        }

        GeneralMethod.pushListBorrow(request);
        return "borrowList.jsp";
    }

}
