package dao;

import model.Post;
import model.dbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    /**
     * Given an <i>username</i> returns all posts created by that profile
     * @param username Username of the profile
     * @return <code>List</code> of post objects; List is empty if no posts could be found.
     */
    public List<Post> getPostsByUser(String username) {
        Connection connection = dbConnector.getConnection();
        List<Post> posts = new ArrayList<>();

        try {
            PreparedStatement postsListStatement = connection.prepareStatement(
                    "select * from posts where owner_name = ? order by data_added desc"); //FIXME: typo in sql file "date" not "data"
            postsListStatement.setString(1, username);
            ResultSet postsListResultSet = postsListStatement.executeQuery();
            if (!postsListResultSet.next()) {
                postsListStatement.close();
                return posts;
            }

            int postID = postsListResultSet.getInt("post_id");
            String postPictureURL = postsListResultSet.getString("post_pic");
            String posterUsername = postsListResultSet.getString("owner_name");
            Date postDate = postsListResultSet.getDate("data_added"); //FIXME: same type as above
            Post post = new Post(postID, postPictureURL, posterUsername, postDate);
            posts.add(post);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return posts;
    }
}
