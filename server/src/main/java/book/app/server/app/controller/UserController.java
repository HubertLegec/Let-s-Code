package book.app.server.app.controller;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import book.app.server.app.dto.NewUserDTO;
import book.app.server.app.dto.UserDTO;
import book.app.server.app.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        return userService.addNewUser(email, password);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/updateUser", method = RequestMethod.PUT)
    public void addUser(@RequestBody UserDTO user) {
        try {
            userService.updateUser(user.getToken(), user.getPassword(), user.getCity(), user.getStreet(), user.getNr());
        } catch (InvalidAttributesException e) {
            wrongToken();
        }
    }

    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT, reason = "Wrong token")
    // 418
    @ExceptionHandler(InvalidAttributesException.class)
    public void wrongToken() {

    }

}
