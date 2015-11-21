package book.app.server.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import book.app.server.app.model.Author;
import book.app.server.app.model.Book;
import book.app.server.app.model.User;

@Repository
public class BookDaoImpl implements BookDao {

    private static final String FIND_AUTHOR_BY_NAME = "select a from Author a where a.name=:name";

    private static final String FIND_BOOKS_BY_OWNER = "select b from Book b where b.owner=:owner";

    private static final String FIND_BOOKS = "select b from Book b where (b.authors like %:author%) or (b.title like %:title%)";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Author findAuthorByName(final String authorName) {
        return (Author) em.createQuery(FIND_AUTHOR_BY_NAME).setParameter("name", authorName).getSingleResult();
    }

    @Override
    @Transactional
    public void save(final Book book) {
        em.merge(book);

    }

    @Override
    public void saveAuthor(final Author author) {
        em.merge(author);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Book> findBooksByOwner(final User user) {
        return (List<Book>) em.createQuery(FIND_BOOKS_BY_OWNER).setParameter("owner", user).getResultList();
    }

    public List<Book> findBooks(String query) {
        return (List<Book>) em.createQuery(FIND_BOOKS).setParameter("author", query).setParameter("title", query).getResultList();
    }
}
