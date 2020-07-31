package controller;

import dao.DBmanager;
import model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;



@Controller
public class AuthentificationController {

    HashMap<Integer, Integer> getUserRating(List<Post> posts, DBmanager db, String user){
        HashMap<Integer, Integer> userRatings = new HashMap<Integer, Integer>();
        for (int i = 0; i< posts.size();i++){
            userRatings.put(posts.get(i).getPost_id(),db.getReview(user, posts.get(i).getPost_id()));
        }
        return userRatings;
    }

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
            mv.addObject("userRatings", getUserRating(posts, db, user));
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
                    @RequestParam String password,
                    @RequestParam MultipartFile profilePic) throws IOException {
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager)sc.getAttribute("db");
        if (!db.nameUsed(username)){
            db.addUser(username, password);
            saveProfilePicture(req, username, profilePic);
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

    /**
     * Helper method. Handles saving profile picture into resources directory during registration.
     * @param req HttpServletRequest
     * @param username Username used for registration
     * @param profilePic Uploaded file for profile picture
     * @throws IOException If an I/O error occurred
     */
    private void saveProfilePicture(HttpServletRequest req, String username, MultipartFile profilePic) throws IOException {
        String path = req.getSession().getServletContext().getRealPath("/")  + "/resources/userData/profilePics/";
        String filename = profilePic.getOriginalFilename();
        assert (filename != null);
        String extension = filename.substring(filename.lastIndexOf("."));
        File file = new File(path + username + extension);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            assert (created);
            profilePic.transferTo(file);
        }
    }

}
