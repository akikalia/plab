package controller;

import dao.DBmanager;
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
import java.util.List;

@Controller
public class ProfileController {
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    ModelAndView search(HttpServletRequest req,
                  HttpServletResponse resp,
                  HttpSession ses,
                  @RequestParam String q){
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        List<String> arr = db.searchProfile(q);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("search-page");
        mv.addObject("profiles", arr);
        return mv;
    }

    @RequestMapping(value = "/addpost", method = RequestMethod.GET)
    String addPostPage(){
        return "add-post-page";
    }

    //don't know yet (problem is uploading file to server, how should I accept it ...)
    @RequestMapping(value = "/addpost", method = RequestMethod.POST)
    String addPost(HttpServletRequest req,
                   HttpServletResponse resp,
                   HttpSession ses){
        //need to save file in resources/userData/posts
        //add post to database and redirect to newsfeed-page
        return "newsfeed-page";
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    ModelAndView getProfile(HttpServletRequest req,
                      HttpServletResponse resp,
                      HttpSession ses,
                      @RequestParam String u){
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        Profile p = db.getProfile(u);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("profile-page");
        mv.addObject("profile", p);
        return mv;
    }


    @RequestMapping(value = "/setRating", method = RequestMethod.POST)
    void setRating(HttpServletRequest req,
                            HttpServletResponse resp,
                            HttpSession ses,
                            @RequestParam int post_id,
                           @RequestParam int rating){
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        db.setReview(((String)ses.getAttribute("user")), post_id, rating);
    }

}
