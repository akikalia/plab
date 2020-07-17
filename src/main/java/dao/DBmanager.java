package dao;

import model.Profile;
import model.dbConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

public class DBmanager {

    public class Post {
        int post_id;
        String owner_name;
        String post_pic;
        Date date_added;
    }

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

    /*
        public User getUserById(int ) {
            Connection con = dbConnector.getConnection();
            String query = "SELECT * FROM users WHERE  = ?;";
            try {
                PreparedStatement statement = con.prepareStatement(query);
                statement.setInt(1, );
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    User us = new User();
                    us.user_name = (String)rs.getObject("user_name");
                    us.password = (String)rs.getObject("password");
                    return us;
                } else
                    return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Set<User> getUsers() {
            Set<User> result = new HashSet<>();
            Connection con = dbConnector.getConnection();
            String query = "SELECT * FROM users;";
                try {
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        User us = new User();
                        us.user_name = (String)rs.getObject("user_name");
                        us.password = (String)rs.getObject("password");
                        result.add(us);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            return result;
        }
    */
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

        String query = "SELECT * FROM posts WHERE owner_name = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.post_id = (int) rs.getObject("post_id");
                p.owner_name = user_name;
                p.date_added = (Date) rs.getObject("date_added");
                p.post_pic = (String) rs.getObject("post_pic");
                result.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Returns valid rating number if the user has reviewed the post
    //-1 otherwise
    public int getReview(String reviewer_name, Post post) {
        Connection con = dbConnector.getConnection();
        String query = "SELECT * FROM reviews WHERE reviewer_name = ? "
                + "AND post_id = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, reviewer_name);
            statement.setInt(2, post.post_id);
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
    public boolean setReview(String reviewer_name, Post post, int rating) {
        Connection con = dbConnector.getConnection();
        if (getReview(reviewer_name, post) == -1) {
            String update = "INSERT INTO reviews (rating, post_id, "
                    + "reviewer_name) VALUES (?, ?, ?);";
            try {
                PreparedStatement stmt = con.prepareStatement(update);
                stmt.setInt(1, rating);
                stmt.setInt(2, post.post_id);
                stmt.setString(3, reviewer_name);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static final int DEF_RATING = 0;

    public int getPostAvgReview(int post_id) {
        Connection con = dbConnector.getConnection();
        String query = "SELECT avg(rating) FROM reviews WHERE post_id = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, post_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if (rs.wasNull())
                return DEF_RATING;
            else
                return (int) rs.getObject(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUserAvgReview(String user_name) {
        Connection con = dbConnector.getConnection();
        String query = "SELECT avg(rating) FROM reviews WHERE owner_name = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if (rs.wasNull())
                return DEF_RATING;
            else
                return (int) rs.getObject(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
        String q = "SELECT * from users WHERE user_name IN (SELECT followee_name FROM follows WHERE follower_name = ?);";
        try {
            PreparedStatement statement = con.prepareStatement(q);
            statement.setString(1, user_name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String name = (String) rs.getObject("user_name");
                result.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Post> getFeedPosts(String user_name) {
        List<Post> result = new ArrayList<>();
        Set<String> followings = getFollowings(user_name);
        for (String followee_name : followings) {
            result.addAll(getUsersPosts(followee_name));
        }
        return result;
    }
/*	
	public User getUser(String user_name, String password) {
		Connection con = dbConnector.getConnection();
		String query = "SELECT * FROM users "
					 + "WHERE user_name = ? AND password = ?;";
		try {
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, user_name);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				User us = new User();
                us.user_name = (String)rs.getObject(2);
                us.password = (String)rs.getObject(3);
				return us;
			} else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
*/

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

        String profilePictureURL = "path before" + username + ".extension"; //FIXME: Change to proper URL
        BigDecimal rating = new BigDecimal(0);
        if (ratingResultSet.next()) rating = ratingResultSet.getBigDecimal("rating");
        int numFollowers = numFollowersResultSet.getInt("num_followers");
        int numFollowing = numFollowingResultSet.getInt("num_following");
        int numReviews = numReviewsResultSet.getInt("num_reviews");
        List<Post> posts = getUsersPosts(username);

        return new Profile(username, profilePictureURL, rating, numFollowers, numFollowing, numReviews, posts);
    }
}