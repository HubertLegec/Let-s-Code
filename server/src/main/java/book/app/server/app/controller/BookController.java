package book.app.server.app.controller;

import java.util.LinkedList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import book.app.server.app.dto.BookDTO;
import book.app.server.app.dto.BookToLendDTO;
import book.app.server.app.dto.UserBook;
import book.app.server.app.service.BookService;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/books", method = RequestMethod.GET)
    public List<UserBook> getBooks(@RequestParam("token") String token) throws InvalidAttributesException {
        return bookService.getBooksByToken(token);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/searchBooks", method = RequestMethod.GET)
    public List<BookToLendDTO> searchBooks(@RequestParam("token") String token, @RequestParam("query") String query)
            throws InvalidAttributesException {
        return bookService.getBooks(token, query);
    }

//    @ResponseBody
//    @ResponseStatus(HttpStatus.OK)
//    @RequestMapping(path = "/test", method = RequestMethod.GET)
//    public BookDTO test() throws InvalidAttributesException {
//        BookDTO book = new BookDTO();
//        authors.add("authro");
//        book.setTitle("asd");
//        book.setToken("asd");
//        book.setYear("2343");
//        return book;
//    }

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
