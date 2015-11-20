package book.app.server.app.dao;

import book.app.server.app.model.Author;

public interface BookDao {

    Author findAuthorByName(final String authorName);

}
