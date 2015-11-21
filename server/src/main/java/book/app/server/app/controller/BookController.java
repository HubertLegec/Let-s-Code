package book.app.server.app.controller;

import book.app.server.app.dto.BookDTO;
import book.app.server.app.model.Book;
import book.app.server.app.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/books", method = RequestMethod.GET)
    public List<Book> getBooks(@RequestParam("token") String token) throws InvalidAttributesException {
        return bookService.getBooksByToken(token);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/searchBooks", method = RequestMethod.GET)
    public List<Book> searchBooks(@RequestParam("query") String query) throws InvalidAttributesException {
        return bookService.getBooks(query);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/addBook", method = RequestMethod.POST)
    public void addBook(@RequestBody BookDTO book) throws InvalidAttributesException {
        bookService.addBook(book.getToken(), book.getAuthors(), book.getTitle(), book.getYear());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong token")
    // 400
    @ExceptionHandler(InvalidAttributesException.class)
    public void wrongToken() {

    }
}
