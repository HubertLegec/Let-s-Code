package book.app.server.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import book.app.server.app.service.BookService;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

//    @ResponseBody
//    @ResponseStatus(HttpStatus.OK)
//    @RequestMapping(path = "/addBook", method = RequestMethod.POST)
//    public String addBook(@RequestBody NewBookDTO book) {
//        // return bookService.addBook(user.getLogin(), user.getPassword());
//    }
}
