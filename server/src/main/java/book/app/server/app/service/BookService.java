package book.app.server.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import book.app.server.app.dao.BookDao;

@Repository
public class BookService {
    @Autowired
    private BookDao userDao;

    public void addBook(final String token, final String author, final String title, final String year) {
        // TODO Auto-generated method stub

    }
}
