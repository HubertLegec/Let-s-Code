package book.app.server.app.dao;

import java.util.List;

import book.app.server.app.model.Token;
import book.app.server.app.model.User;

public interface UserDao {

    public User findUserByLogin(final String login);

    public void save(final User user);

    public User getUserByToken(final String token);

    public List<Token> findTokensByUserId(final long id);

}
