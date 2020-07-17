package controller;

import dao.DBmanager;
import model.Post;
import model.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class AuthentificationController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mainPage(HttpServletRequest req,
                                 HttpServletResponse resp,
                                 HttpSession ses){
        ModelAndView mv = new ModelAndView();
        String user = (String)ses.getAttribute("user");
        if (null == user)
            mv.setViewName("login-page");
        else {
            mv.setViewName("newsfeed-page");
            ServletContext sc = req.getServletContext();
            DBmanager db = (DBmanager) sc.getAttribute("db");
            List<Post> posts = db.getFeedPosts(user);
            mv.addObject("posts", posts);
        }
        return mv;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest req,
                 HttpServletResponse resp,
                 HttpSession ses,
                 @RequestParam String username,
                 @RequestParam String password) throws IOException {

        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        //check if user exists and password matches
        if(db.passwordValidation(username, password)){
            ses.setAttribute("user",username);
        }
        resp.sendRedirect("/");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(HttpServletRequest req,
                    HttpServletResponse resp,
                    HttpSession ses,
                    @RequestParam String username,
                    @RequestParam String password) throws IOException {
        //TODO: need to get profile picture and save in resources/userData/profilePics and name it username
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        if (!db.nameUsed(username)){
            db.addUser(username, password);
            ses.setAttribute("user",username);
        }
        resp.sendRedirect("/");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest req,
                  HttpServletResponse resp,
                  HttpSession ses) throws IOException {
        ses.invalidate();
        resp.sendRedirect("/");
    }

}
