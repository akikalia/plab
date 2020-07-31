package controller;

import dao.DBmanager;
import model.Profile;
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
import java.util.Enumeration;
import java.util.List;

@Controller
public class ProfileController {
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    ModelAndView search(HttpServletRequest req,
                        HttpServletResponse resp,
                        HttpSession ses,
                        @RequestParam String q) {
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager) sc.getAttribute("db");
        List<String> arr = db.searchProfile(q);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("search-page");
        mv.addObject("profiles", arr);
        return mv;
    }

    @RequestMapping(value = "/addpost", method = RequestMethod.GET)
    String addPostPage() {
        return "add-post-page";
    }

    @RequestMapping(value = "/addpost", method = RequestMethod.POST)
    void addPost(HttpServletRequest req,
                   HttpServletResponse resp,
                   HttpSession ses,
                   @RequestParam MultipartFile postPic) throws IOException {
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager) sc.getAttribute("db");
        String username = (String) ses.getAttribute("user");
        int postId = db.getPostsCount() + 1;
        db.addPost(username, username + "_" + postId); //TODO: pic_url is not necessary anymore
        savePostPicture(req, postId, postPic);

        resp.sendRedirect("/");
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    ModelAndView getProfile(HttpServletRequest req,
                            HttpServletResponse resp,
                            HttpSession ses,
                            @RequestParam String u) {
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager) sc.getAttribute("db");
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
                   @RequestParam String post_id,
                   @RequestParam String rating) {
        int rating_ = Integer.parseInt(rating);
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager) sc.getAttribute("db");
        if (rating_ > 5 || rating_ < 0){
            return ;
        }
        db.setReview(((String) ses.getAttribute("user")), Integer.parseInt(post_id), rating_);
    }

    @RequestMapping(value = "/cbChanged", method = RequestMethod.POST)
    void connChanged(HttpServletRequest req, HttpServletResponse resp, @RequestParam String u, @RequestParam String logged_in) {
        Enumeration<String> en = req.getParameterNames();
        ServletContext sc = req.getServletContext();
        DBmanager db = (DBmanager) sc.getAttribute("db");
        while (en.hasMoreElements()) {
            String pn = en.nextElement();
            if (pn.equals("cb")) { // checkbox was passed = checkbox got checked
                db.addConnection(logged_in, u);
                try {
                    resp.sendRedirect("profile?u=" + u);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // checkbox was not passed = checkbox got unchecked
        db.removeConnection(logged_in, u);
        try {
            resp.sendRedirect("profile?u=" + u);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method. Handles saving post picture into resources directory.
     * @param req HttpServletRequest
     * @param postId Unique post id
     * @param postPic Uploaded file for post
     * @throws IOException If an I/O error occurred
     */
    private void savePostPicture(HttpServletRequest req, int postId, MultipartFile postPic) throws IOException {
        String path = req.getSession().getServletContext().getRealPath("/") + "/resources/userData/posts/";
        String filename = postPic.getOriginalFilename();
        assert (filename != null);
        String extension = filename.substring(filename.lastIndexOf("."));
        File file = new File(path + postId + extension);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            assert (created);
            postPic.transferTo(file);
        }
    }
}
