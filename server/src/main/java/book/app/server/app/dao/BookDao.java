package book.app.server.app.dao;

import java.util.List;

import book.app.server.app.model.Author;
import book.app.server.app.model.Book;
import book.app.server.app.model.User;

public interface BookDao {

    public Author findAuthorByName(final String authorName);

    public void save(final Book book);

    public void saveAuthor(final Author author);

    public List<Book> findBooksByOwner(final User user);

}
