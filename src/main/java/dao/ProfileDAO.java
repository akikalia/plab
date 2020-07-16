package dao;

import model.Post;
import model.Profile;
import model.dbConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileDAO {

    /**
     * Given a <i>searchString</i> looks for profiles, usernames of which contain that string and returns them as a List.
     *
     * @param searchString String put in by user, used to compare with profile usernames
     * @return <code>List</code> of profile objects; List is empty if no profile could be found
     */
    public List<Profile> searchProfile(String searchString) {
        Connection connection = dbConnector.getConnection();
        List<Profile> profiles = new ArrayList<>();
        String username;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select user_name from users where user_name like ?;");
            preparedStatement.setString(1, "%" + searchString + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                preparedStatement.close();
                return profiles;
            }

            username = resultSet.getString("user_name");
            Profile profile = buildProfile(connection, username);
            profiles.add(profile);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return profiles;
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
     * Helper method. Given a <i>username</i> extracts necessary values from database and creates a Profile object from them
     *
     * @param connection A connection with a specific database
     * @param username Username of the desired profile
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

        String profilePictureURL = "path before" + username + ".extension"; //FIXME: Change to proper URL
        BigDecimal rating = new BigDecimal(0);
        if (ratingResultSet.next()) rating = ratingResultSet.getBigDecimal("rating");
        int numFollowers = numFollowersResultSet.getInt("num_followers");
        int numFollowing = numFollowingResultSet.getInt("num_following");
        int numReviews = numReviewsResultSet.getInt("num_reviews");
        PostDAO postDAO = new PostDAO();
        List<Post> posts = postDAO.getPostsByUser(username);

        return new Profile(username, profilePictureURL, rating, numFollowers, numFollowing, numReviews, posts);
    }
}
