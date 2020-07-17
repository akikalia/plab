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

@Controller
public class ProfileController {
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    String search(HttpServletRequest req,
                  HttpServletResponse resp,
                  HttpSession ses,
                  @RequestParam String search){
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        //arr = db.search()
        //pass arr to search.jsp (or just do db.search() in search.jsp...)
        return "";
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
        //add post to database and redirect to timeline-page
        return "timeline-page";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    String getProfile(HttpServletRequest req,
                      HttpServletResponse resp,
                      HttpSession ses,
                      @RequestParam String username){
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        //profile p = db.getProfile(username);
        //pass profile to profile.jsp
        //(or just do db.getProfile(username) in profile.jsp...)
        return "profile";
    }

}
