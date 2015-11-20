package book.app.server.app.service;

import java.util.Date;
import java.util.UUID;

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
        // this is the authentication token user will send in order to use the
        // web service
        String userToken = new BCryptPasswordEncoder().encode(key);
        user.addToken(new Token(userToken, user));
        return new String(userToken);

    }

}
