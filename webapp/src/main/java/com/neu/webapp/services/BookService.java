package com.neu.webapp.services;

import com.neu.webapp.errors.BookAdditionStatus;
import com.neu.webapp.models.Book;
import com.neu.webapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.UUID;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }



    public Book CreateBook(Book book) {
        Book b = bookRepository.save(book);
        return book;


    }

    public void UpdateBook(Book book) {
        // get the book that need to be updated
        Book upBook = bookRepository.findById(book.getId()).get();

        //check for fields that need to be updated
        if (book.getAuthor() != null) upBook.setAuthor(book.getAuthor());
        if (book.getIsbn() != null)  upBook.setIsbn(book.getIsbn());
        if (book.getTitle() != null) upBook.setTitle(book.getTitle());
        if (book.getQuantity() != null) upBook.setQuantity(book.getQuantity());

        bookRepository.save(upBook);
    }


    public Book getById(UUID id) {
        try{
            return bookRepository.findById(id).get();
        }catch(Exception exc) {
            return null;
        }
    }

    public void deleteById(UUID id){
        bookRepository.deleteById(id);
    }

    public BookAdditionStatus getStockingStatus(BindingResult errors) {
        FieldError titleError = errors.getFieldError("title");
        FieldError authorError = errors.getFieldError("author");
        FieldError isbnError = errors.getFieldError("isbn");
        FieldError quantityError = errors.getFieldError("quantity");

        String titleErrorMessage = titleError == null ? "-" : titleError.getCode();
        String authorErrorMessage = authorError == null ? "-" : authorError.getCode();
        String isbnErrorMessage = isbnError == null ? "-" : isbnError.getCode();
        String quantityErrorMessage = quantityError == null ? "-" : quantityError.getCode();

        return new BookAdditionStatus(titleErrorMessage, authorErrorMessage, isbnErrorMessage, quantityErrorMessage);
    }
}
