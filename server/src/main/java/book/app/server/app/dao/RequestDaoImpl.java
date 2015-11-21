package book.app.server.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import book.app.server.app.model.Request;
import book.app.server.app.model.User;

/**
 * Created by krzysiek on 21.11.15.
 */
@Repository
public class RequestDaoImpl implements RequestDao {


    private static final String FIND_REQUEST_BY_SENDER = "select r from Request r where r.sender = :sender " +
            "and (r.status = 'ACCEPTED' or r.status = 'REJECTED' or r.status = 'LENT')";
    private static final String FIND_REQUEST_BY_RECEIVER = "select r from Request r where r.book.owner = :receiver " +
            "and (r.status = 'ACTIVE' or r.status = 'ACCEPTED' or r.status = 'LENT')";


    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> findBySender(final User sender) {
        return (List<Request>) em.createQuery(FIND_REQUEST_BY_SENDER).setParameter("sender", sender).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> findByReceiver(final User receiver) {
        return (List<Request>) em.createQuery(FIND_REQUEST_BY_RECEIVER).setParameter("receiver", receiver)
                .getResultList();
    }

    @Override
    @Transactional
    public void save(final Request request) {
        em.merge(request);
    }

    @Override
    public Request findById(final String requestId) {
        return em.find(Request.class, Long.valueOf(requestId));
    }
}
