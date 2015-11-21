package book.app.server.app.service;

import book.app.server.app.dao.BookDao;
import book.app.server.app.dao.UserDao;
import book.app.server.app.dto.BookToLendDTO;
import book.app.server.app.model.Author;
import book.app.server.app.model.Book;
import book.app.server.app.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.naming.directory.InvalidAttributesException;

import java.security.acl.Owner;
import java.util.HashSet;
import java.util.LinkedList;
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

    public List<BookToLendDTO> getBooks(final String token, final String query) {
        User user = userDao.getUserByToken(token);
        List<BookToLendDTO> result = new LinkedList<BookToLendDTO>();
        List<Book> books = bookDao.findBooks(query);
        for (Book book : books) {
            System.out.println("\n\n");
            System.out.println(book.getTitle());
            System.out.println("\n\n");
            if (!book.getOwner().equals(user)
                    && book.getOwner().getAddress().getCity().equals(user.getAddress().getCity())) {
                result.add(new BookToLendDTO(book.getId(), book.getTitle(), prepareAuthors(book.getAuthors()), book
                        .getOwner().getNick(), book.getOwner().getAddress().getCity(), book.getOwner().getAddress()
                        .getStreet()));
            }

        }
        return result;
    }

    private String prepareAuthors(final Set<Author> authors) {
        StringBuilder sb = new StringBuilder();
        for (Author author : authors) {
            sb.append(author.getName());
        }
        return sb.toString();
    }
}
