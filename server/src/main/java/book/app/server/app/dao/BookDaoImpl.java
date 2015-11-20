package book.app.server.app.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import book.app.server.app.model.Author;

@Repository
public class BookDaoImpl implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Author findAuthorByName(String authorName) {
        // TODO Auto-generated method stub
        return null;
    }
}
