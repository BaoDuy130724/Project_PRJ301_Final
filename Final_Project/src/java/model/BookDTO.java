/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class BookDTO {
    private int bookId;
    private String title;
    private String author;
    private String publisher;
    private int yearPublished;
    private String ISBN; //International Standard Book Number: Ma so tieu chuan quoc te cua sach
    private int categoryId;
    private int quantity;
    private int available;
    private boolean isdeleted;
    private String image;
    public BookDTO(){
        
    }

    public BookDTO(int bookId, String title, String author, String publisher, 
            int year, String ISBN, int categoryId, int quantity, int available, boolean isdeleted, String image) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.yearPublished = year;
        this.ISBN = ISBN;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.available = available;
        this.isdeleted = isdeleted;
        this.image = image;
    }

    public BookDTO(String title, String author, String publisher, 
            int yearPublished, String ISBN, int categoryId, int quantity, int available, boolean isdeleted, String image) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
        this.ISBN = ISBN;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.available = available;
        this.isdeleted = isdeleted;
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAvailable() {
        return available;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }
}
