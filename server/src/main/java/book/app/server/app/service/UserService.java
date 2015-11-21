package book.app.server.app.service;

import book.app.server.app.dao.UserDao;
import book.app.server.app.model.Address;
import book.app.server.app.model.Token;
import book.app.server.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.naming.directory.InvalidAttributesException;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class UserService {

    @Autowired
    private UserDao userDao;

    public String addNewUser(final String email, final String password) throws InvalidKeyException {
        User user = new User(email, new BCryptPasswordEncoder().encode(password));
        if (userDao.findUserByLogin(email) != null)
            throw new InvalidKeyException("email is used");
        String userToken = prepareToken(email);
        user.addToken(new Token(userToken, user));
        userDao.save(user);
        return userToken;

    }

    private String prepareToken(final String email) {
        String key = UUID.randomUUID().toString().toUpperCase() + "|" + "someImportantProjectToken" + "|" + email + "|"
                + new Date();
        String userToken = new BCryptPasswordEncoder().encode(key);
        return userToken;
    }

    public void updateUser(final String token, final String password, final String nick, final String city,
            final String street, final String nr) throws InvalidAttributesException {
        User user = userDao.getUserByToken(token);
        if (user == null)
            throw new InvalidAttributesException("user with this token is not available");
        if (password != null && !password.isEmpty())
            user.setPassword(password);
        if (nick != null && !nick.isEmpty())
            user.setNick(nick);
        if (user.getAddress() == null)
            user.setAddress(new Address());
        if (city != null && !city.isEmpty())
            user.getAddress().setCity(city);
        if (street != null && !street.isEmpty())
            user.getAddress().setStreet(street);
        if (nr != null && !nr.isEmpty())
            user.getAddress().setHouseNumber(nr.toString());
        userDao.save(user);
    }

    public String login(String email, String password) throws InvalidKeyException {
        User user = userDao.findUserByLogin(email);

        if (user == null)
            throw new InvalidKeyException("email is wrong");
        else if (!new BCryptPasswordEncoder().matches(password, user.getPassword()))
            throw new InvalidKeyException("password is wrong");
        List<Token> tokens = userDao.findTokensByUserId(user.getId());
        user.setTokens(tokens);
        String token = prepareToken(email);
        user.addToken(new Token(token, user));
        userDao.save(user);
        return token;

    }


}
