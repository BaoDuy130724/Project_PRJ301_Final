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
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.BookDAO;
import model.BookDTO;
import model.UserDTO;
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
            } else if (action.equals("bookSubmitting")) {
                url = handleBookSubmitting(request, response);
            } else if (action.equals("deleteBook")) {
                url = handleBookDeleting(request, response);
            } else if (action.equals("editBook")) {
                url = handleBookEditing(request, response);
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
                GeneralMethod.pushListCategory(request);
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
            GeneralMethod.pushListCategory(request);
            return "productForm.jsp";
        }
        return WELCOME;
    }

    private String handleBookSubmitting(HttpServletRequest request, HttpServletResponse response) {
        boolean isAdd = Boolean.parseBoolean(request.getParameter("isAdd"));
        String url = WELCOME;
        try {
            String errorMessage = validateBookForm(request);
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String publisher = request.getParameter("publisher");
            String ISBN = request.getParameter("ISBN");
            int yearPublished = Integer.parseInt(request.getParameter("year"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int available = Integer.parseInt(request.getParameter("available"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));

            BookDTO book = new BookDTO(title, author, publisher, yearPublished, ISBN, categoryId, quantity, available, false, null);

            if (!isAdd) {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                book.setBookId(bookId);
                BookDTO oldBook = bdao.getBookById(bookId); 
                book.setImage(oldBook.getImage());
            }
            if (errorMessage != null) {
                request.setAttribute("message", errorMessage);
                request.setAttribute("book", book);
                request.setAttribute("isAdd", isAdd);
                GeneralMethod.pushListCategory(request);
                return url;
            }

            Part imagePart = request.getPart("image");
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            String imageFileName = null;

            boolean success;
            if (isAdd) {
                success = bdao.createBook(book);
                System.out.println("Book creation success? " + success);
                if (success) {
                    int bookId = bdao.getLastInsertedBookId(); // Lấy ID để đặt tên file ảnh
                    book.setBookId(bookId);
                    if (fileName != null && !fileName.isEmpty()) {
                        imageFileName = bookId + ".jpg";
                        saveUploadedImage(imagePart, imageFileName, request);
                        bdao.updateBookImage(bookId, imageFileName);
                    }
                }
                request.setAttribute("message", success ? "Book added successfully." : "Failed to add book.");
            } else {
                success = bdao.updateBook(book);
                if (fileName != null && !fileName.isEmpty()) {
                    imageFileName = book.getBookId() + ".jpg";
                    saveUploadedImage(imagePart, imageFileName, request);
                    bdao.updateBookImage(book.getBookId(), imageFileName);
                }
                request.setAttribute("message", success ? "Book updated successfully." : "Failed to update book.");
            }

            if (!success) {
                request.setAttribute("book", book);
                GeneralMethod.pushListCategory(request);
                return "productForm.jsp";
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "An error occurred while processing the book.");
            url = "productForm.jsp"; // BẮT BUỘC set lại URL rõ ràng nếu có lỗi
        }

        request.setAttribute("isAdd", isAdd);
        UserDTO user = GeneralMethod.getCurrentUser(request);
        GeneralMethod.prepareDashboard(request, user.getRole());
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

    private String handleBookDeleting(HttpServletRequest request, HttpServletResponse response) {
        if (!GeneralMethod.isAdmin(request)) {
            GeneralMethod.getAccessDenied(request, "You do not have permission to access this page.");
        }
        String bookId = request.getParameter("bookId");
        try {
            int bookId_value = Integer.parseInt(bookId);
            boolean deleted = bdao.deleteBook(bookId_value);
            if (deleted) {
                request.setAttribute("messDelete", "Successfully Deleted");
            } else {
                request.setAttribute("messDelete", "Unsuccessfully Deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GeneralMethod.prepareDashboard(request, GeneralMethod.getCurrentUser(request).getRole());
        return WELCOME;
    }

    private String handleBookEditing(HttpServletRequest request, HttpServletResponse response) {
        String bookId = request.getParameter("bookId");
        try {
            int bookId_value = Integer.parseInt(bookId);
            BookDTO book = bdao.getBookById(bookId_value);
            request.setAttribute("book", book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handleBookAdding(request, response);
    }

    private void saveUploadedImage(Part imagePart, String imageFileName, HttpServletRequest request) {
        try {
            String savePath = request.getServletContext().getRealPath("/assets/book_images");

            File fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }
            String fullSavePath = savePath + File.separator + imageFileName;

            try ( InputStream is = imagePart.getInputStream();  FileOutputStream fos = new FileOutputStream(fullSavePath)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("Image saved to: " + fullSavePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
    
}
