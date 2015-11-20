package book.app.server.app.service;

import java.util.Date;
import java.util.UUID;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import book.app.server.app.dao.UserDao;
import book.app.server.app.model.Token;
import book.app.server.app.model.User;

public class UserService {

    @Autowired
    private UserDao userDao;

    public String addNewUser(final String login, final String password) {
        User user = new User(login, password);
        if (userDao.findUserByLogin(login) != null)
            return "";
        String key = UUID.randomUUID().toString().toUpperCase() + "|" + "someImportantProjectToken" + "|" + login + "|"
                + new Date();
        String userToken = new BCryptPasswordEncoder().encode(key);
        user.addToken(new Token(userToken, user));
        return new String(userToken);

    }

    public void updateUser(final String token, final String password, final String city, final String street,
            final String string) throws InvalidAttributesException {
        User user = userDao.getUserByToken(token);
        if (user == null)
            throw new InvalidAttributesException("user with this token is not available");
        if (password != null)
            user.setPassword(password);
        if (city != null)
            user.getAddress().setCity(city);
        if (street != null)
            user.getAddress().setStreet(street);
        if (string != null)
            user.getAddress().setHouseNumber(string.toString());
        userDao.save(user);
    }

}
