package book.app.server.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import book.app.server.app.model.Token;
import book.app.server.app.model.User;

@Repository
public class UserDaoImpl implements UserDao {

    private static final String SELECT_USER_BY_LOGIN = "select u from User u where u.email=:email";

    private static final String SELECT_USER_BY_TOKEN = "select t.user from Token t where t.token=:token";

    private static final String SELECT_TOKENS_BY_USER_ID = "select t from Token t where t.user.id=:userId";

    @PersistenceContext
    private EntityManager em;

    @Override
    public User findUserByLogin(final String email) {
        User user = null;
        try {
            user = (User) em.createQuery(SELECT_USER_BY_LOGIN).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public void save(final User user) {
        em.merge(user);

    }

    @Override
    public User getUserByToken(final String token) {
        User user = null;
        try {
            user = (User) em.createQuery(SELECT_USER_BY_TOKEN).setParameter("token", token).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Token> findTokensByUserId(final long userId) {
        return em.createQuery(SELECT_TOKENS_BY_USER_ID).setParameter("userId", userId).getResultList();
    }
}
