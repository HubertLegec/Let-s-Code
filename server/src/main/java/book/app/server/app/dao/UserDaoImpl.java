package book.app.server.app.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl {

    @PersistenceContext
    private EntityManager em;
}
