package book.app.server.app.service;

import book.app.server.app.dao.BookDao;
import book.app.server.app.dao.RequestDao;
import book.app.server.app.dao.UserDao;
import book.app.server.app.dto.BookToLendDTO;
import book.app.server.app.dto.UserBook;
import book.app.server.app.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.naming.directory.InvalidAttributesException;
import java.util.*;

@Repository
public class BookService {
    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RequestDao requestDao;

    public void addBook(final String token, final String authors, final String title, final String year)
            throws InvalidAttributesException {
        User user = userDao.getUserByToken(token);
        List<String> authorsList = parseAuthors(authors);
        // parseAuthors()
        if (user == null)
            throw new InvalidAttributesException("Wrong token");
        Set<Author> authorsOfBook = new HashSet<Author>();
        for (String authorName : authorsList) {
            Author author = bookDao.findAuthorByName(authorName);
            if (author != null)
                authorsOfBook.add(author);
            else
                authorsOfBook.add(new Author(authorName));
        }

        Book book = new Book(authorsOfBook, title, year, user);
        for (Author author : book.getAuthors()) {
            author.addBook(book);
            bookDao.saveAuthor(author);
        }
    }

    private List<String> parseAuthors(String authors) {
        return new LinkedList<String>(Arrays.asList(authors.split(";")));
    }

    public List<UserBook> getBooksByToken(final String token) throws InvalidAttributesException {
        User user = userDao.getUserByToken(token);
        if (user == null)
            throw new InvalidAttributesException("Wrong token");
        List<Book> books = bookDao.findBooksByOwner(user);
        List<UserBook> result = new LinkedList<UserBook>();
        for (Book book : books) {
            book.setAuthors(bookDao.findAuthorsByBookId(book.getId()));
            result.add(new UserBook(book.getId().toString(), book.getTitle(), prepareAuthors(book.getAuthors()), String
                    .valueOf(book.getYear())));
        }
        return result;

    }

    public List<BookToLendDTO> getBooks(final String token, final String query) {
        User user = userDao.getUserByToken(token);
        List<BookToLendDTO> result = new LinkedList<BookToLendDTO>();
        List<Book> books = bookDao.findBooks(query);
        for (Book book : books) {
            book.setAuthors(bookDao.findAuthorsByBookId(book.getId()));
            if (book.getOwner() != null && !book.getOwner().equals(user)
                    ) {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void removeBook(final String token, final String bookId) throws InvalidAttributesException {
        Book book = bookDao.findBookById(Long.valueOf(bookId));
        System.out.println(book);
        User owner = book.getOwner();
        User user = userDao.getUserByToken(token);
        if (user == null || owner.getId() != user.getId())
            throw new InvalidAttributesException();
        user.setBooks(new HashSet(bookDao.findBooksByOwner(user)));
        user.removeBook(Long.valueOf(bookId));
        userDao.save(user);
        Book book1 = bookDao.findBookById(Long.valueOf(bookId));
        book1.setOwner(null);
        book1.setAuthors(null);
        bookDao.save(book1);
        // bookDao.remove(bookDao.findBookById(bookId));
    }

    public void addNewRequest(final String token, final Long bookId) throws InvalidAttributesException {
        Book book = bookDao.findBookById(bookId);
        User sender = userDao.getUserByToken(token);
        if (sender == null)
            throw new InvalidAttributesException();
        Request request = new Request(sender, book);
        request.setStatus(RequestStatus.ACTIVE);
        requestDao.save(request);

    }

    private void updateRequest(final String requestId, final RequestStatus status) {
        Request request = requestDao.findById(requestId);
        request.setStatus(status);
        requestDao.save(request);
    }

    public void applyRequestAction(final String token, final String requestId, final String action) {
        // TODO rewrite this shit!
        if (action.equals("OK")) {
            updateRequest(requestId, RequestStatus.INACTIVE);
        }
        else if (action.equals("ACCEPT")) {
            updateRequest(requestId, RequestStatus.ACCEPTED);
        }
        else if (action.equals("REJECT")) {
            updateRequest(requestId, RequestStatus.REJECTED);
        }
        else if (action.equals("CANCEL")) {
            updateRequest(requestId, RequestStatus.INACTIVE);
        }
        else {
            throw new IllegalArgumentException();
        }


    }
}
