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
import java.util.ArrayList;
import java.util.List;
import model.BookDAO;
import model.BookDTO;
import model.CategoryDAO;
import model.CategoryDTO;
import utils.GeneralMethod;

/**
 *
 * @author Admin
 */
@WebServlet(name = "BookController", urlPatterns = {"/BookController"})
public class BookController extends HttpServlet {

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

    private static final void pushCategoryDAO(HttpServletRequest request) {
        CategoryDAO cdao = new CategoryDAO();
        List<CategoryDTO> listCategories = new ArrayList<>();
        listCategories = cdao.getAllCategories();
        request.setAttribute("listCategories", listCategories);
    }
    BookDAO bdao = new BookDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME;
        String action = request.getParameter("action");
        try {
            if (action.equals("search")) {
                url = handleBookSearching(request, response);
            } else if (action.equals("addBook")) {
                url = handleBookAdding(request, response);
            } else if (action.equals("submitCreateBook")) {
                url = handleBookSubmitting(request, response);
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

    private String handleBookSearching(HttpServletRequest request, HttpServletResponse response) {
        String url = WELCOME;
        try {
            String searchTitle = request.getParameter("txtSearch");
            List<BookDTO> listBooks = new ArrayList<>();
            listBooks = bdao.getBooksByTitle(searchTitle);
            if (listBooks == null || listBooks.isEmpty()) {
                request.setAttribute("message", "No book found!!!!");
            } else {
                pushCategoryDAO(request);
                request.setAttribute("listBooks", listBooks);
                request.setAttribute("searchTitle", searchTitle);

            }
        } catch (Exception e) {
        }
        return url;
    }

    private String handleBookAdding(HttpServletRequest request, HttpServletResponse response) {
        boolean isAdd = Boolean.parseBoolean(request.getParameter("isAdd"));
        if (GeneralMethod.isAdmin(request)) {
            request.setAttribute("isAdd", isAdd);
            pushCategoryDAO(request);
            return "productForm.jsp";
        }
        return WELCOME;
    }

    private String handleBookSubmitting(HttpServletRequest request, HttpServletResponse response) {
        boolean isAdd = Boolean.parseBoolean(request.getParameter("isAdd"));
        String url = "productForm.jsp";
        try {
            String errorMessage = validateBookForm(request);
            if (errorMessage != null) {
                request.setAttribute("message", errorMessage);
                pushCategoryDAO(request);
                return url;
            }
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String publisher = request.getParameter("publisher");
            String ISBN = request.getParameter("ISBN");
            int yearPublished = Integer.parseInt(request.getParameter("year"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int available = Integer.parseInt(request.getParameter("available"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            BookDTO book = new BookDTO(title, author, publisher, yearPublished, ISBN, categoryId, quantity, available);
            boolean success = bdao.createBook(book);
            if (success) {
                request.setAttribute("message", "Book added successfully.");
            } else {
                request.setAttribute("message", "Failed to add book.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("isAdd", isAdd);
        pushCategoryDAO(request);
        return url;
    }

    private String validateBookForm(HttpServletRequest request) {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String isbn = request.getParameter("ISBN");
        String yearStr = request.getParameter("year");
        String quantityStr = request.getParameter("quantity");
        String availableStr = request.getParameter("available");
        String categoryStr = request.getParameter("categoryId");

        if (title == null || title.trim().isEmpty()
                || author == null || author.trim().isEmpty()
                || publisher == null || publisher.trim().isEmpty()
                || isbn == null || isbn.trim().isEmpty()
                || yearStr == null || quantityStr == null || availableStr == null || categoryStr == null) {
            return "Please fill in all fields.";
        }
        try {
            int year = Integer.parseInt(yearStr);
            int quantity = Integer.parseInt(quantityStr);
            int available = Integer.parseInt(availableStr);
            int categoryId = Integer.parseInt(categoryStr);

            if (year < 0 || quantity < 0 || available < 0 || categoryId <= 0) {
                return "Year, quantity, available, and category ID must be non-negative.";
            }
        } catch (NumberFormatException e) {
            return "Year, quantity, available, and category ID must be valid numbers.";
        }
        return null;
    }
}
