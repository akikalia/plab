package main.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DBmanager {
	
	public class User {
		int user_id;
		String username;
		String password;
		String pfp_url;
	}
	
	public class Post {
		int post_id;
		int	user_id;
		String post_pic_url;
		String post_txt;
	}
	
	public DBmanager(){
		Connection con = dbConnector.getConnection();
		try {
			Statement stmt = con.createStatement();
			String update = "CREATE TABLE IF NOT EXISTS users" 
				+ "(user_id int NOT NULL,"
				+ "username varchar(255) NOT NULL UNIQUE,"
				+ "password varchar(255) NOT NULL,"
				+ "pfp_url varchar(255),"
				+ "PRIMARY KEY(user_id));";
			stmt.executeUpdate(update);
			update = "CREATE TABLE IF NOT EXISTS posts"
					+ "(post_id int NOT NULL,"
					+ "user_id int NOT NULL,"
					+ "post_pic_url,"
					+ "post_text,"
					+ "PRIMARY KEY(post_id));";
			stmt.executeUpdate(update);
			update = "CREATE TABLE IF NOT EXISTS reviews"
					+ "(review_id NOT NULL,"
					+ "post_id NOT NULL,"
					+ "from_id NOT NULL,"
					+ "to_id NOT NULL,"
					+ "PRIMARY KEY(review_id));";
			stmt.executeUpdate(update);
			update = "CREATE TABLE IF NOT EXISTS follows"
					+ "(follow_id NOT NULL,"
					+ "from_id NOT NULL,"
					+ "to_id NOT NULL,"
					+ "PRIMARY KEY(follow_id));";
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Table creation error");
		}
	}
	
	public boolean nameUsed(String username) {
		Connection con = dbConnector.getConnection();
		String query = "SELECT * FROM users WHERE username =" + username;
		try {
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public User getUserById(int user_id) {
		Connection con = dbConnector.getConnection();
		String query = "SELECT * FROM users WHERE user_id =" + user_id;
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if(rs.next()) {
				User us = new User();
                us.user_id = (int)rs.getObject(1);
                us.username = (String)rs.getObject(2);
                us.password = (String)rs.getObject(3);
                us.pfp_url = (String)rs.getObject(4);
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

        String query = "SELECT * FROM users";
        Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
	            while (rs.next()){
	                User us = new User();
	                us.user_id = (int)rs.getObject(1);
	                us.username = (String)rs.getObject(2);
	                us.password = (String)rs.getObject(3);
	                us.pfp_url = (String)rs.getObject(4);
	                result.add(us);
	            } 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	//returns true if user was added, false if it already existed
	public boolean addUser(String username, String password) {
		Connection con = dbConnector.getConnection();
        Statement stmt;
		if(nameUsed(username))
			return false;
		String update = "INSERT INTO users (username, password) VALUES (" 
				+ username + ", " + password + ");";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(update);
			return true;
		} catch (SQLException e) {}
		return false; 
	}
	
	public Set<Post> getUsersPosts(int user_id) {
		Set<Post> result = new HashSet<>(); 
		Connection con = dbConnector.getConnection();
 
        String query = "SELECT * FROM posts WHERE user_id = " + user_id;
        Statement stmt;
        try {
	        stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()){
	            Post p = new Post();
	            p.post_id = (int)rs.getObject(1);
	            p.user_id = (int)rs.getObject(2);
	            p.post_pic_url = (String)rs.getObject(3);
	            p.post_txt = (String)rs.getObject(4);
	            result.add(p);
	        }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return result;
	}
	
	//Returns valid rating number if the user has reviewed the post
	//-1 otherwise
	public int getReview (int user_id, int post_id) {
		Connection con = dbConnector.getConnection();
		String query = "SELECT * FROM reviews WHERE user_id =" + user_id
				+ " AND post_id = " + post_id;
		try {
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				return (int)rs.getObject(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	//Returns true if rated successfully
	//false if review already exists
	public boolean setReview (int user_id, int rating, int post_id, int to_id) {
		Connection con = dbConnector.getConnection();
        try {
	        if(getReview(user_id, post_id) == -1) {
	        	String update = "INSERT INTO reviews "
	        			+ "(rating, post_id, from_id, to_id) "
	        			+ "VALUES (?, ?, ?, ?);";
	    	    PreparedStatement stmt = con.prepareStatement(update);
	    	    stmt.setInt(1, rating);
	    	    stmt.setInt(2, post_id);
	    	    stmt.setInt(3, user_id);
	    	    stmt.setInt(4, to_id);	    	    
	    	    stmt.executeUpdate();
	        	return true;
	        } else {
	        	/* uncomment this if a user can review a post
	        	 * more then once
	        	String update = "UPDATE reviews "
	        			+ "SET rating = " + rating
	        			+ "WHERE from_id = " + user_id
	        			+ " AND post_id = " + post_id + ";"; 
	        	stmt.executeUpdate(update);
	        	*/
	        	return false;
	        }
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	//returns 0 if no one has rated yet
	public int getPostAvgReview(int post_id) {
		Connection con = dbConnector.getConnection();
	    Statement stmt;
        try {
	        stmt = con.createStatement();
	        String query = "SELECT avg"
	        		+ "(rating) FROM reviews WHERE post_id = "
	        		+ post_id + ";";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        	if(rs.wasNull())
	        		return 0;
	        	else
	        		return (int)rs.getObject(1);
	        }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		return 0;
	}

	//returns 0 if no one has rated yet
	public int getUserAvgReview(int user_id) {
		Connection con = dbConnector.getConnection();
	    Statement stmt;
        try {
	        stmt = con.createStatement();
	        String query = 
	        		"SELECT avg(rating) FROM reviews WHERE user_id = "+ user_id;
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        	if(rs.wasNull())
	        		return 0;
	        	else
	        		return (int)rs.getObject(1);
	        }
        } catch (SQLException e) {}
		return 0;
	}
	
	public void addPost(int user_id, String pic_url) {
		Connection con = dbConnector.getConnection();
        Statement stmt;
		String update = "INSERT INTO posts (user_id, post_pic_url) VALUES (" 
				+ user_id + ", " + pic_url + ");";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//returns all the users that the given one follows
	public Set<User> getFollowings(int user_id) {
		Set<User> result = new HashSet<User>();
		Connection con = dbConnector.getConnection();
		String q = "SELECT * from users WHERE user_id "
			+ "IN (SELECT to_id FROM follows WHERE from_id = ?);";
        try {
        	PreparedStatement statement = con.prepareStatement(q);
			statement.setString(1, ""+user_id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				User us = new User();
				us.user_id = (int)rs.getObject(1);
                us.username = (String)rs.getObject(2);
                us.password = (String)rs.getObject(3);
                us.pfp_url = (String)rs.getObject(4);
                result.add(us);
			}
		} catch (SQLException e) {
			e.printStackTrace();}
		return null;
	}
	
	public Set<Post> getFeedPosts(int user_id) {
		Set<Post> result = new HashSet<>();
		Set<User> followings = getFollowings(user_id);
		for(User f : followings) {
			result.addAll(getUsersPosts(f.user_id));
		}
		return result;
	}
	
	public User getUser(String username, String password) {
		Connection con = dbConnector.getConnection();
		String query = "SELECT * FROM users WHERE username =" + username
						+ " AND password = " + password + ";";
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if(rs.next()) {
				User us = new User();
                us.user_id = (int)rs.getObject(1);
                us.username = (String)rs.getObject(2);
                us.password = (String)rs.getObject(3);
                us.pfp_url = (String)rs.getObject(4);
				return us;
			} else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
