package book.app.server.app.service;

import book.app.server.app.dao.BookDao;
import book.app.server.app.dao.UserDao;
import book.app.server.app.model.Author;
import book.app.server.app.model.Book;
import book.app.server.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.naming.directory.InvalidAttributesException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                authorsOfBook.add(new Author(authorName));
        }
        Book book = new Book(authorsOfBook, title, year, user);
        bookDao.save(book);
        for (Author author : book.getAuthors()) {
            author.addBook(book);
            bookDao.saveAuthor(author);
        }
    }

    public List<Book> getBooksByToken(final String token) throws InvalidAttributesException {
        User user = userDao.getUserByToken(token);
        if (user == null)
            throw new InvalidAttributesException("Wrong token");
        return bookDao.findBooksByOwner(user);

    }

    public List<Book> getBooks(final String query) {
        return bookDao.findBooks(query);
    }
}
