package book.app.server.app.controller;

import book.app.server.app.dto.NewUserDTO;
import book.app.server.app.dto.UserDTO;
import book.app.server.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.InvalidAttributesException;
import java.security.InvalidKeyException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestBody NewUserDTO user) throws InvalidKeyException {
        return userService.addNewUser(user.getEmail(), user.getPassword());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestBody NewUserDTO user) throws InvalidKeyException {
        return userService.login(user.getEmail(), user.getPassword());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/updateUser", method = RequestMethod.POST)
    public void addUser(@RequestBody UserDTO user) throws InvalidAttributesException {
        userService.updateUser(user.getToken(), user.getPassword(), user.getNick(), user.getCity(), user.getStreet(),
                user.getNr());
    }

    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT, reason = "Wrong token")
    // 418
    @ExceptionHandler(InvalidAttributesException.class)
    public void wrongToken() {

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "email is used")
    // 400
    @ExceptionHandler(InvalidKeyException.class)
    public void usedEmail() {

    }

}
