package book.app.server.app.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class Main {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String getPopularBets() {
        return "Hello wordls";
    }

}
