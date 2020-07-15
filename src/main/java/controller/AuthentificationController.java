package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthentificationController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String mainPage(){
        return "login-page";
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    String login(){
        //compare password and sign in
        return "";
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    String register(){
        //add to database and sign in
        return "";
    }
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    String logout(){
        //sign out
        return "";
    }

}
