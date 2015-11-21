package book.app.server.app.dao;

import book.app.server.app.model.Author;
import book.app.server.app.model.Book;
import book.app.server.app.model.User;

import java.util.List;
import java.util.Set;

public interface BookDao {

    public Author findAuthorByName(final String authorName);

    public void save(final Book book);

    public void saveAuthor(final Author author);

    public List<Book> findBooksByOwner(final User user);

    public List<Book> findBooks(final String query);

    public Book findBookById(final Long bookId);

    public void remove(final Book book);

    public Set<Author> findAuthorsByBookId(final Long id);

}
