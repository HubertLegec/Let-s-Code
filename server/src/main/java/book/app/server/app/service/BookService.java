package book.app.server.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import book.app.server.app.dao.BookDao;

@Repository
public class BookService {
    @Autowired
    private BookDao userDao;
}
