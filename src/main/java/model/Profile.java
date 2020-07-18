package model;

import dao.DBmanager;

import java.math.BigDecimal;
import java.util.List;

public class Profile {
    private final String username;
    private final String profilePictureURL;
    private final BigDecimal rating;
    private final int numFollowers;
    private final int numFollowing;
    private final int numReviews;
    private final List<Post> posts;

    public Profile(String username, String profilePictureURL, BigDecimal rating,
                   int numFollowers, int numFollowing, int numReviews, List<Post> posts) {
        this.username = username;
        this.profilePictureURL = profilePictureURL;
        this.rating = rating;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
        this.numReviews = numReviews;
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
