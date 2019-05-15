package by.tryputs.bookssharing.controllers;

import by.tryputs.bookssharing.entities.Book;
import by.tryputs.bookssharing.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value ="books", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BookController {

    private BookService bookService;

    @PostMapping(value = "/add")
    public ResponseEntity addBook(final Book book) {
        bookService.addBook(book);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }
}