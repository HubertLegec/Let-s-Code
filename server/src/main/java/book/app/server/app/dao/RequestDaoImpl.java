package book.app.server.app.dao;

import book.app.server.app.model.Request;
import book.app.server.app.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

/**
 * Created by krzysiek on 21.11.15.
 */
public class RequestDaoImpl implements RequestDao{

    private static final String FIND_REQUEST_BY_SENDER = "select r from Request r where r.sender = :sender";
    private static final String FIND_REQUEST_BY_RECEIVER = "select r from Request r where r.sender = :sender";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Request> findBySender(User sender) {
        return (List<Request>) em.createQuery(FIND_REQUEST_BY_SENDER).setParameter("sender", sender).getResultList();
    }

    @Override
    public List<Request> findByReceiver(User receiver) {
        return null;
    }
}
