package controller;

import model.DBmanager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class AuthentificationController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String mainPage(){
        return "login-page";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req,
                 HttpServletResponse resp,
                 HttpSession ses,
                 @RequestParam String username,
                 @RequestParam String password) throws IOException {

        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        //check if user exists and password matches
//        if(db.isPasswordCorrect(username, password)){
//            ses.setAttribute("user",username);
//            return "timeline-page";
//        }
//        else
//            resp.sendRedirect("/");
        return "login-page";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest req,
                    HttpServletResponse resp,
                    HttpSession ses,
                    @RequestParam String username,
                    @RequestParam String password) throws IOException {
        //also need to get profile picture and save in resources/userData/profilePics and name it username
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        //need to validate password in a database(make sure it's not there)
        //if not in the database adding current username to session and database
        if (!db.nameUsed(username)){
            db.addUser(username, password);
            ses.setAttribute("user",username);
            return "timeline-page";
        }
        resp.sendRedirect("/");
        return "login-page";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest req,
                  HttpServletResponse resp,
                  HttpSession ses) throws IOException {
        ses.invalidate();
        resp.sendRedirect("/");
    }

}
