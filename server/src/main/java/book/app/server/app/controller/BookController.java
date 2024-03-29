package book.app.server.app.controller;

import book.app.server.app.dto.*;
import book.app.server.app.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.InvalidAttributesException;

import java.security.InvalidKeyException;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/books", method = RequestMethod.GET)
    public List<UserBook> getBooks(@RequestParam(name = "token") String token) throws InvalidAttributesException {
        return bookService.getBooksByToken(token);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/remove", method = RequestMethod.POST)
    public void removeBook(@RequestBody RemoveBookDTO bookToRemove) throws InvalidAttributesException {
        bookService.removeBook(bookToRemove.getToken(), bookToRemove.getBookId());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/searchBooks", method = RequestMethod.GET)
    public List<BookToLendDTO> searchBooks(@RequestParam("token") String token, @RequestParam("query") String query)
            throws InvalidAttributesException {
        if (query == null)
            query = "";
        return bookService.getBooks(token, query);
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

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/addRequest", method = RequestMethod.POST)
    public void addRequest(@RequestBody RequestBookDTO requestBookDTO) throws InvalidKeyException,
            InvalidAttributesException {
        bookService.addNewRequest(requestBookDTO.getToken(), requestBookDTO.getBookId());
    }

    // @ResponseBody
    // @ResponseStatus(HttpStatus.OK)
    // @RequestMapping(path = "/updateRequest", method = RequestMethod.POST)
    // private void addRequest(@RequestBody UpdateRequestDTO requestBookDTO)
    // throws InvalidKeyException,
    // InvalidAttributesException {
    // bookService.updateRequest(requestBookDTO.getRequestId(),
    // requestBookDTO.getStatus());
    // }
    //
    // @ResponseBody
    // @ResponseStatus(HttpStatus.OK)
    // @RequestMapping(path = "/updateRequestStatus", method =
    // RequestMethod.POST)
    // public void updateRequestStatus(@RequestBody UpdateRequestStatusDTO
    // updateRequestStatusDTO) throws InvalidKeyException,
    // InvalidAttributesException {
    // bookService.updateRequest(requestBookDTO.getRequestId(),
    // requestBookDTO.getStatus());
    // }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/requestAction", method = RequestMethod.POST)
    public void updateRequestStatus(@RequestBody UpdateRequestStatusDTO updateRequestStatusDTO)
            throws InvalidKeyException, InvalidAttributesException {
        bookService.applyRequestAction(updateRequestStatusDTO.getToken(), updateRequestStatusDTO.getRequestId(),
                updateRequestStatusDTO.getAction());
    }

}
