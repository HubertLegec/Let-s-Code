package book.app.server.app.dao;

import book.app.server.app.model.Request;
import book.app.server.app.model.RequestStatus;
import book.app.server.app.model.User;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by krzysiek on 21.11.15.
 */
@Repository
public class RequestDaoImpl implements RequestDao {


    private static final String FIND_REQUEST_BY_SENDER = "select r from Request r where r.sender = :sender " +
            "and (r.status = 'ACCEPTED' or r.status = 'REJECTED')";
    private static final String FIND_REQUEST_BY_RECEIVER = "select r from Request r where r.book.owner = :receiver " +
            "and (r.status = 'ACTIVE' or r.status = 'ACCEPTED')";


    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Request> findBySender(final User sender) {
        return (List<Request>) em.createQuery(FIND_REQUEST_BY_SENDER).setParameter("sender", sender).getResultList();
    }

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
        return em.find(Request.class, requestId);
    }
}
