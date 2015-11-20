package book.app.server.app.controller;

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
import book.app.server.app.service.BookService;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/books", method = RequestMethod.GET)
    public List<BookDTO> getBooks(@RequestParam("token") String token) {
        // try {
        // bookService.getBooksByToken(token);
        // } catch (InvalidAttributesException e) {
        // // TODO Auto-generated catch block
        // wrongToken();
        // }
        return null;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/addBook", method = RequestMethod.POST)
    public void addBook(@RequestBody BookDTO book) {
        try {
            bookService.addBook(book.getToken(), book.getAuthors(), book.getTitle(), book.getYear());
        } catch (InvalidAttributesException e) {
            // TODO Auto-generated catch block
            wrongToken();
        }
    }

    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT, reason = "Wrong token")
    // 418
    @ExceptionHandler(InvalidAttributesException.class)
    public void wrongToken() {

    }
}
