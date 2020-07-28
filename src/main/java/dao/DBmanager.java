package dao;

import model.Post;
import model.Profile;
import model.dbConnector;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

public class DBmanager {

    public DBmanager() {
    }

    public boolean nameUsed(String user_name) {
        Connection con = dbConnector.getConnection();
        String query = "SELECT * FROM users WHERE user_name = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //returns true if user was added, false if name is used
    public boolean addUser(String user_name, String password) {
        if (nameUsed(user_name))
            return false;

        Connection con = dbConnector.getConnection();
        String update = "INSERT INTO users (user_name, password) "
                + "VALUES (?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(update);
            statement.setString(1, user_name);
            statement.setString(2, password);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
        }

        return false;
    }

    public List<Post> getUsersPosts(String user_name) {
        List<Post> result = new ArrayList<>();
        Connection con = dbConnector.getConnection();

        String query = "SELECT * FROM posts WHERE owner_name = ? order by date_added desc;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setPost_id((int) rs.getObject("post_id"));
                p.setOwner_name(user_name);
                p.setDate_added((Timestamp) rs.getObject("date_added"));
                p.setPost_pic((String) rs.getObject("post_pic"));
                result.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Post> getFeedPosts(String user_name) {
        List<Post> result = new ArrayList<>();
        Connection con = dbConnector.getConnection();

        String query = "Select * from posts where owner_name in (select followee_name from connections where follower_name = ?);";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setPost_id((int) rs.getObject("post_id"));
                p.setOwner_name((String) rs.getObject("owner_name"));
                p.setDate_added((Timestamp) rs.getObject("date_added"));
                p.setPost_pic((String) rs.getObject("post_pic"));
                result.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Returns valid rating number if the user has reviewed the post
    //-1 otherwise
    public int getReview(String reviewer_name, int post_id) {
        Connection con = dbConnector.getConnection();
        String query = "SELECT * FROM reviews WHERE reviewer_name = ? "
                + "AND post_id = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, reviewer_name);
            statement.setInt(2, post_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return (int) rs.getObject("rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //Returns true if rated successfully
    //false if review already exists
    public boolean setReview(String reviewer_name, int post_id, int rating) {
        Connection con = dbConnector.getConnection();
        if (getReview(reviewer_name, post_id) == -1) {
            String update = "INSERT INTO reviews (rating, post_id, "
                    + "reviewer_name) VALUES (?, ?, ?);";
            try {
                PreparedStatement stmt = con.prepareStatement(update);
                stmt.setInt(1, rating);
                stmt.setInt(2, post_id);
                stmt.setString(3, reviewer_name);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void addPost(String user_name, String pic_url) {
        Connection con = dbConnector.getConnection();
        String update = "INSERT INTO posts (owner_name, post_pic) VALUES (?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(update);
            statement.setString(1, user_name);
            statement.setString(2, pic_url);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //returns all the usernames that the given one follows
    public Set<String> getFollowings(String user_name) {
        Set<String> result = new HashSet<String>();
        Connection con = dbConnector.getConnection();
        String q = "SELECT followee_name FROM connections WHERE follower_name = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(q);
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String name = (String) rs.getObject(1);
                result.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addConnection(String follower, String followee) {
        Connection con = dbConnector.getConnection();
        String q = "INSERT INTO connections (follower_name, followee_name) VALUES (?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(q);
            statement.setString(1, follower);
            statement.setString(2, followee);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeConnection(String follower, String followee) {
        Connection con = dbConnector.getConnection();
        String q = "DELETE FROM connections WHERE follower_name = ? AND followee_name = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(q);
            statement.setString(1, follower);
            statement.setString(2, followee);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a <i>searchString</i> looks for profiles, usernames of which contain that string and returns them as a List.
     *
     * @param searchString String put in by user, used to compare with profile usernames
     * @return <code>List</code> of profile objects; List is empty if no profile could be found
     */
    public List<String> searchProfile(String searchString) {
        Connection connection = dbConnector.getConnection();
        List<String> users = new ArrayList<>();
        String username;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select user_name from users where user_name like ?;");
            preparedStatement.setString(1, "%" + searchString + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                username = resultSet.getString("user_name");
                users.add(username);
            }
            preparedStatement.close();
            return users;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    /**
     * Given an <i>username</i> find the profile in database and returns its object.
     *
     * @param username Username of the desired profile
     * @return <code>Profile</code> object with the username
     */
    public Profile getProfile(String username) {
        Connection connection = dbConnector.getConnection();
        Profile profile = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where user_name = ?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                preparedStatement.close();
                return null;
            }

            profile = buildProfile(connection, username);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return profile;
    }

    /**
     * Checks if a profile with <i>username</i> is registered and if <i>password</i> is correct for that profile
     *
     * @param username Username put in by user
     * @param password Password put in by user
     * @return <code>true</code> if profile by <i>username</i> exists and <i>password</i> is correct for that profile;
     * <code>false</code> if profile could not be found or <i>password is incorrect</i>
     */
    public boolean passwordValidation(String username, String password) {
        Connection connection = dbConnector.getConnection();
        boolean canLogin = false;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where user_name = ?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                preparedStatement.close();
            } else {
                String passwordInDB = resultSet.getString("password");
                canLogin = password.equals(passwordInDB);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return canLogin;
    }

    /**
     * Counts how many posts there are in database. Used during post picture naming.
     *
     * @return Number of posts in database
     */
    public int getPostsCount() {
        Connection connection = dbConnector.getConnection();
        int count = -1;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) posts_count FROM posts;");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt("posts_count");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return count;
    }

    /**
     * Helper method. Given an <i>username</i> extracts necessary values from database and creates a Profile object from them
     *
     * @param connection A connection with a specific database
     * @param username   Username of the desired profile
     * @return <code>Profile</code> object built from Result Set
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    private Profile buildProfile(Connection connection, String username) throws SQLException {
        PreparedStatement ratingStatement = connection.prepareStatement(
                "select avg(rating) as rating from reviews r left join posts p on p.post_id = r.post_id where p.owner_name = ?;");
        ratingStatement.setString(1, username);
        ResultSet ratingResultSet = ratingStatement.executeQuery();

        PreparedStatement numFollowersStatement = connection.prepareStatement(
                "select count(*) num_followers from connections where followee_name = ?;");
        numFollowersStatement.setString(1, username);
        ResultSet numFollowersResultSet = numFollowersStatement.executeQuery();
        numFollowersResultSet.next();

        PreparedStatement numFollowingStatement = connection.prepareStatement(
                "select count(*) num_following from connections where follower_name = ?;");
        numFollowingStatement.setString(1, username);
        ResultSet numFollowingResultSet = numFollowingStatement.executeQuery();
        numFollowingResultSet.next();

        PreparedStatement numReviewsStatement = connection.prepareStatement(
                "select count(*) as num_reviews from reviews r left join posts p on p.post_id = r.post_id where p.owner_name = ?;");
        numReviewsStatement.setString(1, username);
        ResultSet numReviewsResultSet = numReviewsStatement.executeQuery();
        numReviewsResultSet.next();

        String profilePictureURL = "path before" + username + ".extension"; //TODO: Not necessary anymore
        BigDecimal rating = new BigDecimal(0);
        if (ratingResultSet.next()) rating = ratingResultSet.getBigDecimal("rating");
        int numFollowers = numFollowersResultSet.getInt("num_followers");
        int numFollowing = numFollowingResultSet.getInt("num_following");
        int numReviews = numReviewsResultSet.getInt("num_reviews");
        List<Post> posts = getUsersPosts(username);

        return new Profile(username, profilePictureURL, rating, numFollowers, numFollowing, numReviews, posts);
    }
}