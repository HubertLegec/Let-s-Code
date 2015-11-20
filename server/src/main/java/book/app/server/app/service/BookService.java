package book.app.server.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import book.app.server.app.dao.BookDao;
import book.app.server.app.dao.UserDao;
import book.app.server.app.model.Author;
import book.app.server.app.model.Book;
import book.app.server.app.model.User;

@Repository
public class BookService {
    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserDao userDao;

    public void addBook(final String token, final List<String> authors, final String title, final String year)
            throws InvalidAttributesException {
        User user = userDao.getUserByToken(token);
        if (user == null)
            throw new InvalidAttributesException("Wrong token");
        Set<Author> authorsOfBook = new HashSet<Author>();
        for (String authorName : authors) {
            Author author = bookDao.findAuthorByName(authorName);
            if (author != null)
                authorsOfBook.add(author);
            else
                authorsOfBook.add(new Author());
        }
        Book book = new Book(authorsOfBook, title, year, user);
    }

    public void getBooksByToken(final String token) {
        // TODO Auto-generated method stub

    }
}
